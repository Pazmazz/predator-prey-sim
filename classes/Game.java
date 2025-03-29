/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 */
package classes;

import java.util.UUID;
import java.util.HashMap;

public class Game extends Console implements Runnable {
	private Thread mainThread;
	private GameScreen screen;
	private String sessionId;
	private HashMap<DebugPriority, Boolean> listeningDebugPriorities;

	private boolean debugModeEnabled = false;
	private GameState state = GameState.INITIAL;
	
	// Update frames
	public MovementFrame movementFrame;

	// All game settings
	public GameSettings settings;

	// Internal states
	public static enum GameState {
		INITIAL,
		LOADED,
		RUNNING,
		PAUSED,
		TERMINATED,
	}

	public static enum DebugPriority {
		LOW,
		MEDIUM,
		HIGH
	}

	/*
	 * Game():
	 * 
	 * A constructor method for the main Game class to initialize 
	 * prerequisite conditions for the game such as UI components
	 * and the game loop thread.
	 */
	public Game() {
		// IMPORTANT: `settings` must be defined first, since other classes reference it
		settings = new GameSettings();
		sessionId = UUID.randomUUID().toString();

		screen = new GameScreen(this);
		mainThread = new Thread(this);
		movementFrame = new MovementFrame();

		// Default listening debug priorities
		listeningDebugPriorities = new HashMap<>();
		listeningDebugPriorities.put(DebugPriority.LOW, Boolean.TRUE);
		listeningDebugPriorities.put(DebugPriority.MEDIUM, Boolean.TRUE);
		listeningDebugPriorities.put(DebugPriority.HIGH, Boolean.TRUE);

		this.setState(GameState.LOADED);
	}

	/*
	 * start():
	 * 
	 * A no-arg method for running the main game loop thread.
	 */
	public void start() {
		if (isLoaded()) {
			this.setState(GameState.RUNNING);
			mainThread.start();
		} else {
			error("start() can only be called once per game instance");
		}
	}

	/*
	 * terminate():
	 * 
	 * A no-arg method for terminating the game loop in the current game thread.
	 */
	public void terminate() {
		if (isRunning() || isPaused()) {
			setState(GameState.TERMINATED);
		} else {
			error("terminate() can only be called if the game is running or paused");
		}
	}

	/*
	 * run():
	 * 
	 * A required method override from the `Runnable` interface which
	 * is called once the `gameThread.start()` method is called. 
	 * 
	 * This method serves as the main game loop, which is responsible
	 * for updating game steps, rendering frames, and handling all other
	 * incremental game logic.
	 * 
	 * TODO: Create separate deltaTimes and time steps for frames operating
	 * on different Hz
	 */
	@Override
	public void run() {
		while (isThreadRunning()) {
			double preSimulationTick = System.currentTimeMillis();
			movementFrame.pulse();
			double postSimulationTick = System.currentTimeMillis();

			double threadYieldTime = settings.SIMULATION_INTERVAL_MILLISECONDS - (postSimulationTick - preSimulationTick);

			if (threadYieldTime > 0) {
				try {
					Thread.sleep((long) threadYieldTime);
					debugPrint("Heartbeat for game instance", sessionId, threadYieldTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/* -------------- */
	/* Getter methods */
	/* -------------- */
	public GameScreen getScreen() {
		return screen;
	}

	public GameState getState() {
		return state;
	}

	/* ------------------------- */
	/* Boolean condition methods */
	/* ------------------------- */
	public boolean isRunning() {
		return state == GameState.RUNNING;
	}

	public boolean isThreadRunning() {
		return isRunning() || isPaused();
	}

	public boolean isInitial() {
		return state == GameState.INITIAL;
	}

	public boolean isLoaded() {
		return state == GameState.LOADED;
	}

	public boolean isPaused() {
		return state == GameState.PAUSED;
	}

	public boolean isTerminated() {
		return state == GameState.TERMINATED;
	}

	public boolean isDebugMode() {
		return debugModeEnabled;
	}

	/* -------------- */
	/* Setter methods */
	/* -------------- */
	public void setState(GameState newState) {
		state = newState;
	}

	public void setDebugModeEnabled(boolean enabled) {
		debugModeEnabled = enabled;
	}

	/* --------------- */
	/* Utility methods */
	/* --------------- */
	public void hideDebugPriority(DebugPriority priority) {
		listeningDebugPriorities.put(priority, Boolean.FALSE);
	}

	public void showDebugPriority(DebugPriority priority) {
		listeningDebugPriorities.put(priority, Boolean.TRUE);
	}

	public boolean isShowingDebugPriority(DebugPriority priority) {
		return listeningDebugPriorities.get(priority);
	}

	public void setDebugPriority(DebugPriority priority) {
		for (DebugPriority key : listeningDebugPriorities.keySet()) {
			if (priority == key) {
				listeningDebugPriorities.put(key, Boolean.TRUE);
			} else {
				listeningDebugPriorities.put(key, Boolean.FALSE);
			}
		}
	}

	public void debugPrint(DebugPriority priority, Object ...messages) {
		if (isDebugMode() && isShowingDebugPriority(priority)) {
			println(
				"%s: %s".formatted(
					settings.debugPrefixes.get(priority), 
					Formatter.concatArray(messages, " | ")
				)
			);
		}
	}

	public void debugPrint(Object ...messages) {
		debugPrint(DebugPriority.LOW, messages);
	}
}
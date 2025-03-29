/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 */
package classes;

import java.util.UUID;

public class Game extends Application implements Runnable {
	private Thread mainThread;
	private GameScreen screen;
	private String sessionId;

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
			Console.error("start() can only be called once per game instance");
		}
	}

	/*
	 * terminate():
	 * 
	 * A no-arg method for terminating the game loop in the current game thread.
	 */
	public void terminate() {
		if (isThreadRunning()) {
			setState(GameState.TERMINATED);
		} else {
			Console.error("terminate() can only be called if the game is running or paused");
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
	 */
	@Override
	public void run() {
		while (isThreadRunning()) {
			double preSimulationTick = tick();
			movementFrame.pulse();
			double postSimulationTick = tick();

			double threadYieldTime = settings.SIMULATION_INTERVAL_MILLISECONDS - (postSimulationTick - preSimulationTick);

			if (threadYieldTime > 0) {
				wait(threadYieldTime);
				Console.debugPrint("Heartbeat for game instance", sessionId, threadYieldTime);
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

	/* -------------- */
	/* Setter methods */
	/* -------------- */
	public void setState(GameState newState) {
		state = newState;
	}
}
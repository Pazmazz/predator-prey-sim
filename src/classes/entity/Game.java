/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 */
package classes.entity;

import classes.abstracts.Application;
import classes.abstracts.FrameProcessor;
import classes.settings.GameSettings;
import classes.settings.GameSettings.SimulationType;
import classes.simulation.MovementFrame;
import classes.simulation.RenderFrame;
import classes.simulation.SimulatedLagFrame;
import classes.util.Console;
import classes.util.Time;
import java.util.UUID;

public class Game extends Application implements Runnable {
	final private Thread mainThread;
	final private GameScreen screen;
	final private String sessionId;
	final private GameSettings settings;
	final private CellGrid gameGrid;

	private GameState state = GameState.INITIAL;
	
	// Update frames
	public MovementFrame movementFrame;
	public RenderFrame renderFrame;
	public SimulatedLagFrame simulatedLagFrame;
	public FrameProcessor[] frameProcesses;

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

		gameGrid = new CellGrid(settings.getGridSize());

		movementFrame = new MovementFrame(this, SimulationType.MOVEMENT);
		renderFrame = new RenderFrame(this, SimulationType.RENDER);
		simulatedLagFrame = new SimulatedLagFrame(this, SimulationType.SIMULATED_LAG);

		frameProcesses = new FrameProcessor[] {
			movementFrame,
			renderFrame,
			simulatedLagFrame
		};

		state = GameState.LOADED;
	}

	/*
	 * start():
	 * 
	 * A method for running the main game loop thread.
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
	 * A method for terminating the game loop in the current game thread.
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
			long simulationDelta = 0;

			for (FrameProcessor frame : frameProcesses) {
				long frameDelta = frame.pulse();
				if (frameDelta > -1) simulationDelta += frameDelta;
			}

			long threadYieldTime = Time.secondsToNano(settings.getSimulation().getFPS()) - simulationDelta;

			if (threadYieldTime > 0) {
				wait(Time.nanoToMillisecond(threadYieldTime));
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

	public String getSessionId() {
		return sessionId;
	}

	public GameSettings getSettings() {
		return settings;
	}

	public CellGrid getGameGrid() {
		return gameGrid;
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
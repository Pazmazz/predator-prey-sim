/*
 * @written 3/28/2025
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

/**
 * This class instantiates the entire game context. All methods for interacting
 * with the game and all game state is managed through the instance of this
 * class.
 */
public class Game extends Application implements Runnable {

	final private Thread mainThread;
	final private GameScreen screen;
	final private String sessionId;
	final private GameSettings settings;
	final private CellGrid gameGrid;

	private GameState state = GameState.INITIAL;

	//
	// Update frames
	//
	public MovementFrame movementFrame;
	public RenderFrame renderFrame;
	public SimulatedLagFrame simulatedLagFrame;
	public FrameProcessor[] frameProcesses;

	//
	// Internal states
	//
	public static enum GameState {
		INITIAL,
		LOADED,
		RUNNING,
		PAUSED,
		TERMINATED,
	}

	public Game() {
		// IMPORTANT: settings must be defined first, since other classes reference it
		this.settings = new GameSettings();

		this.sessionId = UUID.randomUUID().toString();
		this.screen = new GameScreen(this);
		this.mainThread = new Thread(this);
		this.gameGrid = new CellGrid(settings.getGridSize());

		this.movementFrame = new MovementFrame(this, SimulationType.MOVEMENT);
		this.renderFrame = new RenderFrame(this, SimulationType.RENDER);
		this.simulatedLagFrame = new SimulatedLagFrame(this, SimulationType.SIMULATED_LAG);

		this.frameProcesses = new FrameProcessor[] {
				movementFrame,
				renderFrame,
				simulatedLagFrame
		};

		this.state = GameState.LOADED;
	}

	/**
	 * Begins running the game loop and sets the game state from {@code LOADED}
	 * to {@code RUNNING}
	 *
	 * @throws Error if this method is called more than once
	 */
	public void start() {
		if (isLoaded()) {
			this.setState(GameState.RUNNING);
			mainThread.start();
		} else {
			Console.error("start() can only be called once per game instance");
		}
	}

	/**
	 * Terminates the game loop by setting the game state to {@code TERMINATED}
	 */
	public void terminate() {
		this.setState(GameState.TERMINATED);
	}

	/**
	 * A required method override from the {@code Runnable} interface which is
	 * called once the {@code start} method is called.
	 *
	 * <p>
	 * This method serves as the main game loop, which is responsible for
	 * updating game steps, rendering frames, and handling all other incremental
	 * game logic.
	 */
	@Override
	public void run() {
		while (isThreadRunning()) {
			long simulationDelta = 0;

			for (FrameProcessor frame : this.frameProcesses) {
				long frameDelta = frame.pulse();
				if (frameDelta != -1) {
					simulationDelta += frameDelta;
				}
			}

			long threadYieldTime = Time.secondsToNano(settings.getSimulation().getFPS()) - simulationDelta;

			if (threadYieldTime > 0) {
				wait(Time.nanoToMillisecond(threadYieldTime));
			}
		}
	}

	//
	// Public getters
	//
	public GameScreen getScreen() {
		return this.screen;
	}

	public GameState getState() {
		return this.state;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public GameSettings getSettings() {
		return this.settings;
	}

	public CellGrid getGameGrid() {
		return this.gameGrid;
	}

	//
	// Public logic checks
	//
	public boolean isRunning() {
		return this.state == GameState.RUNNING;
	}

	public boolean isThreadRunning() {
		return isRunning() || isPaused();
	}

	public boolean isInitial() {
		return this.state == GameState.INITIAL;
	}

	public boolean isLoaded() {
		return this.state == GameState.LOADED;
	}

	public boolean isPaused() {
		return this.state == GameState.PAUSED;
	}

	public boolean isTerminated() {
		return this.state == GameState.TERMINATED;
	}

	//
	// Public setters
	//
	public void setState(GameState newState) {
		this.state = newState;
	}
}

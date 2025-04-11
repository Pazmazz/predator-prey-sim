/*
 * @written 3/28/2025
 */
package classes.entity;

import classes.abstracts.FrameProcessor;
import classes.abstracts.FrameProcessor.FrameState;
import classes.abstracts.FrameProcessor.Task;
import classes.settings.GameSettings;
import classes.settings.GameSettings.SimulationType;
import classes.simulation.MovementFrame;
import classes.simulation.RenderFrame;
import classes.simulation.SimulatedLagFrame;
import classes.util.Console;
import classes.util.Time;
import classes.entity.CellGrid.Cell;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This class instantiates the entire game context. All methods for interacting
 * with the game and all game state is managed through the instance of this
 * class.
 */
public class Game implements Runnable {

	final private Thread mainThread;
	private GameScreen screen;
	final private String sessionId;
	final private GameSettings settings;
	final private CellGrid gameGrid;
	final private ArrayList<Snapshot> snapshots = new ArrayList<>();

	private GameState state = GameState.INITIAL;

	private long simulationFPS;

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
		this.mainThread = new Thread(this);
		this.gameGrid = new CellGrid(settings.getGridSize()).populate();

		this.movementFrame = new MovementFrame(this, SimulationType.MOVEMENT);
		this.renderFrame = new RenderFrame(this, SimulationType.RENDER);
		this.simulatedLagFrame = new SimulatedLagFrame(this, SimulationType.SIMULATED_LAG);
		this.simulationFPS = Time.secondsToNano(settings.getSimulation().getFPS());

		this.frameProcesses = new FrameProcessor[] {
				movementFrame,
				renderFrame,
				simulatedLagFrame
		};

		this.state = GameState.LOADED;
	}

	public void initializeGameScreen() {
		this.screen = new GameScreen(this);
	}

	// TODO: Implement snapshot saving/loading
	public void saveSnapshot() {
		Snapshot snapshot = new Snapshot();

		this.snapshots.add(snapshot);
	}

	public void loadSnapshot() {

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
			throw new Error("start() can only be called once per game instance");
		}
	}

	/**
	 * Terminates the game loop by setting the game state to {@code TERMINATED}
	 */
	public void terminate() {
		this.setState(GameState.TERMINATED);
		Console.println("TERMINATED APPLICATION");
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
			if (FrameProcessor.isAllSuspended())
				continue;

			long simulationDelta = 0;

			for (FrameProcessor frame : this.frameProcesses) {
				if (frame.isSuspended())
					continue;

				long frameDelta = frame.pulse();
				if (frameDelta != -1)
					simulationDelta += frameDelta;
			}

			long threadYieldTime = this.simulationFPS - simulationDelta;
			if (threadYieldTime > 0)
				wait(Time.nanoToMillisecond(threadYieldTime));
		}
	}

	// TODO: Add documentation
	public void wait(double milliseconds) {
		try {
			Thread.sleep((long) milliseconds);
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}

	// TODO: Implement game grid initializer
	public void initializeGameGrid() {
		ArrayList<Cell> antCells = this.gameGrid
				.getRandomAvailableCells(this.settings.getInitialAnts());

		for (Cell cell : antCells)
			cell.setOccupant(new Ant(this));

		ArrayList<Cell> doodlebugCells = this.gameGrid
				.getRandomAvailableCells(this.settings.getInitialDoodlebugs());

		for (Cell cell : doodlebugCells)
			cell.setOccupant(new Doodlebug(this));
	}

	// TODO: Add documentation
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

	// TODO: Add documentation
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

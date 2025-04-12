/*
 * @written 3/28/2025
 */
package classes.entity;

import classes.abstracts.RunService;
import classes.abstracts.RunService.FrameState;
import classes.abstracts.RunService.Task;
import classes.settings.GameSettings;
import classes.settings.GameSettings.SimulationType;
import classes.simulation.MovementFrame;
import classes.simulation.RenderFrame;
import classes.simulation.SimulatedLagFrame;
import classes.util.Console;
import classes.util.Console.DebugPriority;
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

	final private static Game game = new Game();

	final private Thread mainThread;
	final private String sessionId;
	final private GameSettings settings;
	final private CellGrid gameGrid;
	final private ArrayList<Snapshot> snapshots = new ArrayList<>();

	private GameScreen screen;
	private GameState state = GameState.INITIAL;

	private long simulationFPS;

	//
	// Update frames
	//
	public MovementFrame movementFrame;
	public RenderFrame renderFrame;
	public SimulatedLagFrame simulatedLagFrame;
	public RunService[] frameProcesses;

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

	private Game() {
		/*
		 * SETTINGS MUST BE DEFINED FIRST
		 * 
		 * In case other classes need to reference 'game.settings' within the
		 * constructor
		 */
		this.settings = new GameSettings();

		/*
		 * GAME METADATA
		 * 
		 * Descriptive info about the game instance
		 */
		this.sessionId = UUID.randomUUID().toString();
		this.mainThread = new Thread(this);
		this.simulationFPS = Time.secondsToNano(settings.getSimulation().getFPS());
		this.gameGrid = new CellGrid(settings.getGridSize());

		/*
		 * RUN SERVICE
		 * 
		 * Instantiate the run service simulation frames, which is where all game loop
		 * logic is processed and updated
		 */
		this.movementFrame = new MovementFrame(SimulationType.MOVEMENT);
		this.renderFrame = new RenderFrame(SimulationType.RENDER);
		this.simulatedLagFrame = new SimulatedLagFrame(SimulationType.SIMULATED_LAG);

		this.frameProcesses = new RunService[] {
				movementFrame,
				renderFrame,
				simulatedLagFrame
		};

		/*
		 * CONFIG
		 * 
		 * Configure initial settings in any classes that should update their settings
		 * before the game starts
		 */
		Console.setDebugModeEnabled(true);
		Console.setConsoleColorsEnabled(true);
		Console.hideDebugPriority(DebugPriority.LOW);
		Console.hideDebugPriority(DebugPriority.MEDIUM);

		/*
		 * FINAL
		 * 
		 * Perform any final actions that should occur at the end of the game's
		 * instantiation
		 */
		Console.benchmark("Game grid initializer", this::initializeGameGrid);
		Console.benchmark("Game grid ASCII", this.gameGrid::toASCII);
		Console.benchmark("Game screen", this::initializeGameScreen);

		this.state = GameState.LOADED;
	}

	public String initializeGameScreen() {
		this.screen = new GameScreen();
		return "Game screen benchmark";
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
	public String start() {
		if (isLoaded()) {
			this.setState(GameState.RUNNING);
			mainThread.start();
		} else {
			throw new Error("start() can only be called once per game instance");
		}
		return "Start game thread benchmark";
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
			if (RunService.isAllSuspended())
				continue;

			long simulationDelta = 0;

			for (RunService frame : this.frameProcesses) {
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
	private void wait(double milliseconds) {
		try {
			Thread.sleep((long) milliseconds);
		} catch (InterruptedException e) {
			throw new Error(e);
		}
	}

	// TODO: Implement game grid initializer
	public String initializeGameGrid() {
		this.gameGrid.populate();

		ArrayList<Cell> antCells = this.gameGrid
				.getRandomAvailableCells(this.settings.getInitialAnts());

		for (Cell cell : antCells)
			cell.setOccupant(new Ant());

		ArrayList<Cell> doodlebugCells = this.gameGrid
				.getRandomAvailableCells(this.settings.getInitialDoodlebugs());

		for (Cell cell : doodlebugCells)
			cell.setOccupant(new Doodlebug());

		return "Initialize game grid benchmark";
	}

	// TODO: Add documentation
	//
	// Public getters
	//
	public static Game getInstance() {
		return game;
	}

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

/*
 * @written 3/28/2025
 */
package classes.entity;

import classes.abstracts.Entity;
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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * This class instantiates the entire game context. All methods for interacting
 * with the game and all game state is managed through the instance of this
 * class.
 */
public class Game implements Runnable {

	private static Game game = new Game();
	private GameSettings settings;
	private CellGrid gameGrid;

	final private Thread mainThread;
	final private String sessionId;
	final private ArrayList<Snapshot> snapshots = new ArrayList<>();

	private GameScreen screen;
	private GameState state = GameState.INITIAL;

	private long simulationFPS;
	private long upTime;

	//
	// Update frames
	//
	private MovementFrame movementFrame;
	private RenderFrame renderFrame;
	private SimulatedLagFrame simulatedLagFrame;
	private RunService[] frameProcesses;

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
		this.sessionId = UUID.randomUUID().toString();
		this.upTime = Time.tick();
		this.mainThread = new Thread(this);
		this.state = GameState.LOADED;
	}

	// TODO: Add documentation
	public String initConfig() {
		this.settings = new GameSettings();

		Console.setDebugModeEnabled(true);
		Console.setConsoleColorsEnabled(false);
		Console.hideDebugPriority(DebugPriority.LOW);
		// Console.hideDebugPriority(DebugPriority.MEDIUM);

		return "Game config benchmark";
	}

	public String createGameGrid() {
		this.gameGrid = new CellGrid(this.settings.getGridSize());
		return "Game grid benchmark";
	}

	// TODO: Optimize
	public String initGameGrid() {
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

	public String initRunService() {
		this.simulationFPS = Time.secondsToNano(settings.getSimulation().getFPS());

		this.movementFrame = new MovementFrame(SimulationType.MOVEMENT);
		this.renderFrame = new RenderFrame(SimulationType.RENDER);
		this.simulatedLagFrame = new SimulatedLagFrame(SimulationType.SIMULATED_LAG);

		this.frameProcesses = new RunService[] {
				movementFrame,
				renderFrame,
				simulatedLagFrame
		};

		return "RunService benchmark";
	}

	public String initGameScreen() {
		this.screen = new GameScreen();
		return "Game screen benchmark";
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

	// TODO: Implement snapshot saving/loading
	public void saveSnapshot() {
		Snapshot snapshot = new Snapshot();

		this.snapshots.add(snapshot);
	}

	public void loadSnapshot() {

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
			if (threadYieldTime > 0) {
				try {
					Thread.sleep((long) Time.nanoToMillisecond(threadYieldTime));
				} catch (InterruptedException e) {
					throw new Error(e);
				}
			}
		}
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

	public long getUpTime() {
		return this.upTime;
	}

	public MovementFrame getMovementFrame() {
		return this.movementFrame;
	}

	public RenderFrame getRenderFrame() {
		return this.renderFrame;
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

	@Override
	public String toString() {
		return "Game#" + this.sessionId;
	}
}

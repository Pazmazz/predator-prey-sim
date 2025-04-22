/*
 * @written 3/28/2025
 */
package classes.entity;

import classes.abstracts.Entity;
import classes.abstracts.FrameRunner;
import classes.settings.GameSettings;
import classes.simulation.MovementFrame;
import classes.simulation.RenderFrame;
import classes.util.Console;
import classes.util.Console.DebugPriority;
import classes.util.Time;
import classes.entity.CellGrid.Cell;
import classes.entity.ValueMeter.MeterResetType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * This class instantiates the entire game context. All methods for interacting
 * with the game and all game state is managed through the instance of this
 * class.
 */
@SuppressWarnings("unused")
public class Game implements Runnable {

	final private static Game game = new Game();
	private GameSettings settings;
	private CellGrid gameGrid;

	final private Thread mainThread;
	final private String sessionId;

	// TODO: Implement game history snapshots
	final private ArrayList<String> snapshots = new ArrayList<>();
	private int currentSnapshot = 0;
	// final private int snapshotInterval = 1;

	private GameScreen screen;
	private GameStatus status = GameStatus.INITIAL;
	private SimulationState simulationState = SimulationState.INITIAL;

	final public EventSignal onSimulationStateChanged = new EventSignal();
	final public EventSignal onSimulationEnd = new EventSignal();
	final public EventSignal onGameStatusChanged = new EventSignal();

	private long upTime;
	private long gameHertz;

	//
	// Update frames
	//
	private MovementFrame movementFrame;
	private RenderFrame renderFrame;
	private FrameRunner[] frameProcesses;

	//
	// Internal states
	//
	public static enum GameStatus {
		INITIAL,
		LOADED,
		RUNNING,
		PAUSED,
		TERMINATED,
	}

	public static enum SimulationState {
		INITIAL,
		MANUAL,
		STARTED,
		RUNNING,
		ENDED,
		PAUSED,
		EDITING,
	}

	private Game() {
		this.sessionId = UUID.randomUUID().toString();
		this.upTime = Time.tick();
		this.mainThread = new Thread(this);
	}

	// TODO: Add documentation
	private String initConfig() {
		this.settings = new GameSettings();
		return "Game config benchmark";
	}

	private String createGameGrid() {
		this.gameGrid = new CellGrid(this.settings.getGridSize());
		return "Game grid benchmark";
	}

	// TODO: Optimize
	private String initGameGrid() {
		this.gameGrid.clearCells();
		this.gameGrid.populate();

		ArrayList<Cell> randomCells = this.gameGrid.getRandomAvailableCells(
				this.settings.getInitialEntityCount());

		ValueMeter bufferedAntCount = new ValueMeter(
				0,
				this.settings.getInitialAntCount(),
				0,
				MeterResetType.NONE);
		ValueMeter bufferedDoodlebugCount = new ValueMeter(
				0,
				this.settings.getInitialDoodlebugCount(),
				0,
				MeterResetType.NONE);

		Iterator<Cell> it = randomCells.iterator();
		while (it.hasNext()) {
			Cell cell = it.next();
			if (!bufferedAntCount.isMaxValue()) {
				bufferedAntCount.increment();
				cell.setOccupant(new Ant());
			} else if (!bufferedDoodlebugCount.isMaxValue()) {
				bufferedDoodlebugCount.increment();
				cell.setOccupant(new Doodlebug());
			} else {
				break;
			}
		}

		this.saveSnapshot();
		return "Initialize game grid benchmark";
	}

	public String initRunService() {
		this.gameHertz = Time.secondsToNano(this.settings.getGameHertz());
		this.movementFrame = new MovementFrame();
		this.renderFrame = new RenderFrame();

		this.frameProcesses = new FrameRunner[] {
				movementFrame,
				renderFrame,
		};

		this.setSimulationState(SimulationState.INITIAL);
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
		if (this.isLoaded()) {
			this.setStatus(GameStatus.RUNNING);
			mainThread.start();
			Console.close();
		} else {
			throw new Error("start() can only be called once per game instance");
		}
		return "Start game thread benchmark";
	}

	// TODO: Implement snapshot saving/loading
	public void saveSnapshot() {
		if (this.snapshots.size() >= this.getSettings().getGridSnapshotHistory()) {
			this.snapshots.remove(0);
		}
		String serializedGrid = gameGrid.download();
		this.snapshots.add(serializedGrid);
		this.setMostRecentSnapshot();
	}

	public void setMostRecentSnapshot() {
		this.currentSnapshot = this.snapshots.size();
	}

	public int getMostRecentSnapshot() {
		return this.snapshots.size();
	}

	public int getCurrentSnapshot() {
		return this.currentSnapshot;
	}

	public boolean onCurrentSnapshot() {
		return this.currentSnapshot == this.snapshots.size();
	}

	public void loadSnapshot(int index) {
		this.getGameGrid().upload(this.snapshots.get(index));
	}

	public void loadMostRecentSnapshot() {
		this.loadSnapshot(this.getMostRecentSnapshot() - 1);
	}

	public void loadNextSnapshot() {
		if (this.currentSnapshot < this.snapshots.size()) {
			this.loadSnapshot(this.currentSnapshot);
			this.currentSnapshot++;
		} else {
			this.movementFrame.setDeltaTimeInSeconds(this.getSettings().getManualTimeStepDelta());
			this.movementFrame.step();
		}
		Console.println("$text-green Current Snapshot: $text-white " + this.currentSnapshot,
				"$text-green Out Of: $text-white " + this.snapshots.size());
	}

	public void loadPrevSnapshot() {
		if (this.currentSnapshot > 1) {
			this.currentSnapshot--;
			this.loadSnapshot(this.currentSnapshot - 1);
		}
		Console.println("$text-green Current Snapshot: $text-white " + this.currentSnapshot,
				"$text-green Out Of: $text-white " + this.snapshots.size());
	}

	/**
	 * Terminates the game loop by setting the game state to {@code TERMINATED}
	 */
	public void terminate() {
		this.setStatus(GameStatus.TERMINATED);
		Console.println("TERMINATED APPLICATION");
		Console.close();
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
		long startTime = Time.tick();

		while (isThreadRunning()) {
			this.upTime = Time.tick() - startTime;
			if (FrameRunner.isAllSuspended())
				continue;

			long simulationDelta = 0;

			for (FrameRunner frame : this.frameProcesses) {
				if (frame.isSuspended())
					continue;
				else if (this.isTerminated())
					break;

				long frameDelta = frame.pulse();
				if (frameDelta != -1)
					simulationDelta += frameDelta;
			}

			long threadYieldTime = this.gameHertz - simulationDelta;
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

	public GameStatus getStatus() {
		return this.status;
	}

	public SimulationState getSimulationState() {
		return this.simulationState;
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
		return this.status == GameStatus.RUNNING;
	}

	public boolean isThreadRunning() {
		return this.isRunning() || isPaused();
	}

	public boolean isInitial() {
		return this.status == GameStatus.INITIAL;
	}

	public boolean isLoaded() {
		return this.status == GameStatus.LOADED;
	}

	public boolean isPaused() {
		return this.status == GameStatus.PAUSED;
	}

	public boolean isTerminated() {
		return this.status == GameStatus.TERMINATED;
	}

	//
	// Public setters
	//
	public void setStatus(GameStatus status) {
		this.status = status;
		this.onGameStatusChanged.fire(status);
	}

	public void setSimulationState(SimulationState state) {
		this.simulationState = state;
		this.onSimulationStateChanged.fire(state);
	}

	public void boot() {
		this.onGameStatusChanged.setDispatchQueueEnabled(true);
		this.onSimulationStateChanged.setDispatchQueueEnabled(true);

		Console.benchmark("Creating game grid", this::initConfig);

		// Avg: ~0.001s
		Console.benchmark("Creating game grid", this::createGameGrid);

		// Avg: ~0.005s
		Console.benchmark("Initializing RunService", this::initRunService);

		// Avg: ~0.3s
		Console.benchmark("Initializing game screen", this::initGameScreen);

		// Avg: ~0.02s
		// Console.benchmark("Initializing game grid", this::initGameGrid);

		// Avg: ~0.01s
		// Console.benchmark("Render game grid", this.getGameGrid()::toASCII);
		this.onGameStatusChanged.setDispatchQueueEnabled(false);
		this.onSimulationStateChanged.setDispatchQueueEnabled(false);
	}

	@Override
	public String toString() {
		return "Game#" + this.sessionId;
	}
}

/*
 * @written 3/28/2025
 */
package classes.entity;

import classes.abstracts.Entity;
import classes.abstracts.FrameRunner;
import classes.abstracts.Entity.EntityVariant;
import classes.settings.GameSettings;
import classes.simulation.MovementFrame;
import classes.simulation.RenderFrame;
import classes.util.Console;
import classes.util.ObjectStream;
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
	private GameState state;
	private GameSettings settings;

	final private Thread mainThread;
	final private String sessionId;

	final private ArrayList<GameSnapshot> snapshots = new ArrayList<>();
	private int currentSnapshot = 0;

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
		this.state = new GameState();
	}

	// TODO: Add documentation
	private String initConfig() {
		this.settings = new GameSettings();
		return "Game config benchmark";
	}

	private String createGameGrid() {
		this.getState().setGameGrid(new CellGrid(this.settings.getGridSize()));
		return "Game grid benchmark";
	}

	// TODO: Optimize
	public String initGameGrid() {
		CellGrid grid = this.getState().getGameGrid();
		grid.clearCells();
		grid.populate();

		ArrayList<Cell> randomCells = grid.getRandomAvailableCells(
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
		this.snapshots.add(new GameSnapshot());
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
		this.snapshots.get(index).load();
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

	public long getUpTime() {
		return this.upTime;
	}

	public MovementFrame getMovementFrame() {
		return this.movementFrame;
	}

	public RenderFrame getRenderFrame() {
		return this.renderFrame;
	}

	public GameState getState() {
		return this.state;
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
		// Console.benchmark("Render game grid",
		// this.getState().getGameGrid()::toASCII);
		this.onGameStatusChanged.setDispatchQueueEnabled(false);
		this.onSimulationStateChanged.setDispatchQueueEnabled(false);
	}

	@Override
	public String toString() {
		return "Game#" + this.sessionId;
	}

	public class GameSnapshot {
		private String serializedGameGrid;
		private String serializedCurrentAntMVP;
		private String serializedCurrentDoodlebugMVP;
		private GameState gameStateCopy;

		public GameSnapshot() {
			GameState state = getState();
			this.serializedGameGrid = state.getGameGrid().download();
			if (state.getCurrentAntMVP() != null)
				this.serializedCurrentAntMVP = state.getCurrentAntMVP().serialize();
			if (state.getCurrentDoodlebugMVP() != null)
				this.serializedCurrentDoodlebugMVP = state.getCurrentDoodlebugMVP().serialize();
			this.gameStateCopy = state.clone();
		}

		public String getSerializedGameGrid() {
			return this.serializedGameGrid;
		}

		public void load() {
			GameState state = getState();
			state.getGameGrid().upload(this.serializedGameGrid);
			state.setCurrentAntMVP((Ant) ObjectStream.deserialize(this.serializedCurrentAntMVP).get(0));
			state.setCurrentDoodlebugMVP(
					(Doodlebug) ObjectStream.deserialize(this.serializedCurrentDoodlebugMVP).get(0));
			state.setTotalAnts(this.gameStateCopy.getTotalAnts());
			state.setTotalBugs(this.gameStateCopy.getTotalBugs());
			state.setTotalEntities(this.gameStateCopy.getTotalEntities());
			state.setTotalDoodlebugs(this.gameStateCopy.getTotalDoodlebugs());
			state.setTotalRuntime(this.gameStateCopy.getTotalRuntime());
		}
	}

	public class GameState implements Cloneable {
		private CellGrid gameGrid;

		private long totalRuntime = 0;
		private int totalEntities = 0;
		private int totalBugs = 0;
		private int totalAnts = 0;
		private int totalDoodlebugs = 0;
		private EntityVariant roundWinner;
		private Ant currentAntMVP;
		private Doodlebug currentDoodlebugMVP;

		public EventSignal onChanged = new EventSignal();
		public EventSignal onEntityCountChanged = new EventSignal();

		public void setCurrentAntMVP(Ant currentAntMVP) {
			this.onChanged.fire(this);
			this.currentAntMVP = currentAntMVP;
		}

		public void setCurrentDoodlebugMVP(Doodlebug currentDoodlebugMVP) {
			this.onChanged.fire(this);
			this.currentDoodlebugMVP = currentDoodlebugMVP;
		}

		public void setRoundWinner(EntityVariant roundWinner) {
			this.onChanged.fire(this);
			this.roundWinner = roundWinner;
		}

		public void setTotalAnts(int totalAnts) {
			this.onChanged.fire(this);
			this.onEntityCountChanged.fire(this);
			this.totalAnts = totalAnts;
		}

		public void setTotalBugs(int totalBugs) {
			this.onChanged.fire(this);
			this.onEntityCountChanged.fire(this);
			this.totalBugs = totalBugs;
		}

		public void setTotalDoodlebugs(int totalDoodlebugs) {
			this.onChanged.fire(this);
			this.onEntityCountChanged.fire(this);
			this.totalDoodlebugs = totalDoodlebugs;
		}

		public void setTotalEntities(int totalEntities) {
			this.onChanged.fire(this);
			this.onEntityCountChanged.fire(this);
			this.totalEntities = totalEntities;
		}

		public void setTotalRuntime(long totalRuntime) {
			this.onChanged.fire(this);
			this.totalRuntime = totalRuntime;
		}

		public void setGameGrid(CellGrid gameGrid) {
			this.gameGrid = gameGrid;
		}

		public Ant getCurrentAntMVP() {
			return this.currentAntMVP;
		}

		public Doodlebug getCurrentDoodlebugMVP() {
			return this.currentDoodlebugMVP;
		}

		public CellGrid getGameGrid() {
			return this.gameGrid;
		}

		public EventSignal getOnChanged() {
			return this.onChanged;
		}

		public EventSignal getOnEntityCountChanged() {
			return this.onEntityCountChanged;
		}

		public EntityVariant getRoundWinner() {
			return this.roundWinner;
		}

		public int getTotalAnts() {
			return this.totalAnts;
		}

		public int getTotalBugs() {
			return this.totalBugs;
		}

		public int getTotalDoodlebugs() {
			return this.totalDoodlebugs;
		}

		public int getTotalEntities() {
			return this.totalEntities;
		}

		public long getTotalRuntime() {
			return this.totalRuntime;
		}

		public double getTotalRuntimeInSeconds() {
			return Math.floor(Time.nanoToSeconds(this.totalRuntime) * 100) / 100;
		}

		@Override
		protected GameState clone() {
			try {
				return (GameState) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new Error(e);
			}
		}
	}
}

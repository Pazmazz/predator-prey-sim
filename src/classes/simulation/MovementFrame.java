/*
 * @written 3/29/2025
 */
package classes.simulation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import classes.abstracts.Bug;
import classes.abstracts.Entity;
import classes.abstracts.FrameRunner;
import classes.abstracts.Properties;
import classes.abstracts.Entity.EntityVariant;
import classes.abstracts.Properties.Property;
import classes.entity.Ant;
import classes.entity.CellGrid;
import classes.entity.Doodlebug;
import classes.entity.CellGrid.Cell;
import classes.entity.Game.SimulationState;
import classes.entity.Game;
import classes.entity.Titan;
import classes.entity.TweenData;
import classes.entity.Vector2;
import classes.settings.GameSettings;
import classes.util.Console;
import classes.util.Math2;
import classes.util.Time;

/**
 * This implements the {@code step} method for FrameProcessor. All code that
 * handles what should happen on every Movement step in the simulation should be
 * written in this class's step method.
 * 
 * This frame can be thought of as a "game state update" process. It's goal is
 * to update all game state variables in the data model.
 */
@SuppressWarnings("unused")
public class MovementFrame extends FrameRunner {
	final private static Game game = Game.getInstance();
	final private static GameSettings settings = game.getSettings();

	// STATS
	private EntityVariant winner;
	private Doodlebug doodlebugMVP = new Doodlebug();
	private Ant antMVP = new Ant();

	private int totalBugs = 0;
	private int totalAnts = 0;
	private int totalDoodlebugs = 0;
	private int totalEntities = 0;
	private long totalRuntime = 0;

	public MovementFrame() {
		super(
				settings.getSimulationProcessName(),
				settings.getSimulationFPS(),
				settings.getSimulationDebugInfo());

		game.onSimulationStateChanged.connect(data -> {
			SimulationState state = (SimulationState) data[0];
			if (state == SimulationState.INITIAL) {
				this.doodlebugMVP = new Doodlebug();
				this.antMVP = new Ant();
				this.totalRuntime = 0;
				this.totalAnts = 0;
				this.totalDoodlebugs = 0;
				this.totalEntities = 0;
				this.totalBugs = 0;
				game.getScreen().updateRealtimeStats();
			}
		});

		this.onPostSimulation(task -> {
			game.getScreen().updateRealtimeStats();
		});
	}

	public long getTotalRuntime() {
		return this.totalRuntime;
	}

	public double getTotalRuntimeInSeconds() {
		return Math.floor(Time.nanoToSeconds(this.totalRuntime) * 100) / 100;
	}

	public int getTotalAnts() {
		return totalAnts;
	}

	public int getTotalBugs() {
		return totalBugs;
	}

	public int getTotalDoodlebugs() {
		return totalDoodlebugs;
	}

	public int getTotalEntities() {
		return totalEntities;
	}

	public Doodlebug getCurrentDoodlebugMVP() {
		return this.doodlebugMVP;
	}

	public Ant getCurrentAntMPV() {
		return this.antMVP;
	}

	public EntityVariant getWinner() {
		return this.winner;
	}

	public void setWinner(EntityVariant winner) {
		this.winner = winner;
	}

	public void setCurrentDoodlebugMVP(Doodlebug db) {
		this.doodlebugMVP = db;
	}

	public void setCurrentAntMVP(Ant ant) {
		this.antMVP = ant;
	}

	public void setTotalAnts(int totalAnts) {
		this.totalAnts = totalAnts;
	}

	public void setTotalBugs(int totalBugs) {
		this.totalBugs = totalBugs;
	}

	public void setTotalDoodlebugs(int totalDoodlebugs) {
		this.totalDoodlebugs = totalDoodlebugs;
	}

	public void setTotalEntities(int totalEntities) {
		this.totalEntities = totalEntities;
	}

	@Override
	public void step() {
		this.totalRuntime += getDeltaTime();
		CellGrid grid = game.getGameGrid();
		// grid.collectCells();

		Collection<Cell> cells = grid.getGrid().values();
		long currentTime = Time.tick();
		boolean moveOccurred = false;
		int doodlebugCount = 0;
		int antCount = 0;
		int bugCount = 0;
		int titanCount = 0;
		int entityCount = 0;

		for (Cell cell : cells) {
			Entity<?> entity = cell.getOccupant();
			entityCount++;

			if (entity instanceof Bug) {
				bugCount++;
				Bug<?> bug = (Bug<?>) entity;
				double movementCooldown = bug.getMovementCooldown();
				long lastMoved = bug.getTimeLastMoved();

				// movement cooldown checks
				if (currentTime - lastMoved >= Time.secondsToNano(movementCooldown)) {
					boolean success = bug.move();
					if (success && !moveOccurred) {
						moveOccurred = true;
					}
					if (!bug.hasCell())
						continue;
					bug.setTimeLastMoved();
				}
				bug.incrementTimeInSimulation(this.getDeltaTime());

				// update turn stats
				if (bug instanceof Doodlebug) {
					doodlebugCount++;
					Doodlebug db = (Doodlebug) bug;
					int antsEaten = db.getAntsEatenMeter().getValue();

					if (antsEaten > this.doodlebugMVP.getAntsEatenMeter().getValue()) {
						this.setCurrentDoodlebugMVP(db);
					}
				} else if (bug instanceof Ant) {
					antCount++;
					Ant ant = (Ant) bug;
					long timeInSim = ant.getTimeInSimulation();

					if (timeInSim > this.antMVP.getTimeInSimulation()) {
						this.setCurrentAntMVP(ant);
					}
				}
			}
		}

		setTotalAnts(antCount);
		setTotalDoodlebugs(doodlebugCount);
		setTotalEntities(entityCount);
		setTotalBugs(bugCount);

		if (moveOccurred) {
			game.saveSnapshot();
		}

		// doodlebugs win
		if (bugCount == 0) {
			this.setWinner(EntityVariant.DOODLEBUG);
			game.setSimulationState(SimulationState.ENDED);
			// ants win
		} else if (antCount == game.getSettings().getGridCellCount()) {
			this.setWinner(EntityVariant.ANT);
			game.setSimulationState(SimulationState.ENDED);

			// titans win
		} else if (antCount == 0 && doodlebugCount == 0 && titanCount > 0) {
			this.setWinner(EntityVariant.TITAN);
			game.setSimulationState(SimulationState.ENDED);
		}

	}
}

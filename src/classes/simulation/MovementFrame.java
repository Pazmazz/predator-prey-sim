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
	private Doodlebug doodlebugMVP = new Doodlebug();
	private Ant antMVP = new Ant();

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
			}
		});
	}

	public Doodlebug getCurrentDoodlebugMVP() {
		return this.doodlebugMVP;
	}

	public Ant getCurrentAntMPV() {
		return this.antMVP;
	}

	public void setCurrentDoodlebugMVP(Doodlebug db) {
		this.doodlebugMVP = db;
	}

	public void setCurrentAntMVP(Ant ant) {
		this.antMVP = ant;
	}

	@Override
	public void step() {
		CellGrid grid = game.getGameGrid();
		grid.collectCells();

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
				if (currentTime - lastMoved > Time.secondsToNano(movementCooldown)) {
					bug.move();
					bug.setTimeLastMoved();
					moveOccurred = true;
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

		if (moveOccurred)
			game.saveSnapshot();

		// doodlebugs win
		if (bugCount == 0) {
			game.setSimulationState(SimulationState.ENDED);
			game.onSimulationEnd.fire(EntityVariant.DOODLEBUG, doodlebugMVP, antMVP);
			// ants win
		} else if (antCount == bugCount) {
			game.setSimulationState(SimulationState.ENDED);
			game.onSimulationEnd.fire(EntityVariant.ANT, doodlebugMVP, antMVP);

			// titans win
		} else if (antCount == 0 && doodlebugCount == 0 && titanCount > 0) {
			game.setSimulationState(SimulationState.ENDED);
			game.onSimulationEnd.fire(EntityVariant.TITAN, doodlebugMVP, antMVP);
		}

	}
}

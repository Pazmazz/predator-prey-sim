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
import classes.entity.Ant;
import classes.entity.CellGrid;
import classes.entity.Doodlebug;
import classes.entity.CellGrid.Cell;
import classes.entity.Game.SimulationState;
import classes.entity.Timestamp;
import classes.entity.Game;
import classes.entity.Titan;
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

	public MovementFrame() {
		super(
				game.getSettings().getSimulationProcessName(),
				game.getSettings().getSimulationFPS(),
				game.getSettings().getSimulationDebugInfo());

		// game.onSimulationStateChanged.connect(data -> {
		// SimulationState state = (SimulationState) data[0];
		// if (state == SimulationState.INITIAL) {
		// game.getScreen().updateRealtimeStats();
		// }
		// });
	}

	@Override
	public void step() {
		game.getState().setTotalRuntime(game.getState().getTotalRuntime() + this.getDeltaTime());
		CellGrid grid = game.getState().getGameGrid();
		// grid.collectCells();

		Collection<Cell> cells = grid.getCellsReadOnly();
		// long currentTime = Time.tick();
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
				double movementSpeed = bug.getMovementSpeed();
				Timestamp lastMoved = bug.getTimeLastMoved();

				if (lastMoved.hasTimeElapsed(movementSpeed)) {
					boolean success = bug.move();
					if (success && !moveOccurred) {
						moveOccurred = true;
					}
					if (!bug.hasAssignedCell())
						continue;
					bug.getTimeLastMoved().update();
				}
				bug.getTimeInSimulationMeter().incrementBy(this.getDeltaTime());

				// update turn stats
				if (bug instanceof Doodlebug) {
					doodlebugCount++;
					Doodlebug db = (Doodlebug) bug;
					int antsEaten = (int) db.getAntsEatenMeter().getValue();

					if (antsEaten > game.getState().getCurrentDoodlebugMVP().getAntsEatenMeter().getValue()) {
						game.getState().setCurrentDoodlebugMVP(db);
					}
				} else if (bug instanceof Ant) {
					antCount++;
					Ant ant = (Ant) bug;
					long timeInSim = (long) ant.getTimeInSimulationMeter().getValue();

					if (timeInSim > game.getState().getCurrentAntMVP().getTimeInSimulationMeter().getValue()) {
						game.getState().setCurrentAntMVP(ant);
					}
				}
			}
		}

		game.getState().setTotalEntities(entityCount);
		game.getState().setTotalBugs(bugCount);
		game.getState().setTotalAnts(antCount);
		game.getState().setTotalDoodlebugs(doodlebugCount);
		game.getScreen().updateRealtimeStats();

		if (moveOccurred) {
			game.saveSnapshot();
		}

		// doodlebugs win
		if (bugCount == 0) {
			game.getState().setRoundWinner(EntityVariant.DOODLEBUG);
			game.setSimulationState(SimulationState.ENDED);
			// ants win
		} else if (antCount == game.getSettings().getGridCellCount()) {
			game.getState().setRoundWinner(EntityVariant.ANT);
			game.setSimulationState(SimulationState.ENDED);

			// titans win
		} else if (antCount == 0 && doodlebugCount == 0 && titanCount > 0) {
			game.getState().setRoundWinner(EntityVariant.TITAN);
			game.setSimulationState(SimulationState.ENDED);
		}

	}
}

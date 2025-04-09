/*
 * @written 3/29/2025
 */
package classes.simulation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import classes.abstracts.Bug;
import classes.abstracts.FrameProcessor;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.CellOccupant;
import classes.entity.Game;
import classes.entity.Unit2;
import classes.settings.GameSettings.SimulationType;
import classes.util.Console;

/**
 * This implements the {@code step} method for FrameProcessor. All code that
 * handles what should happen on every Movement step in the simulation should be
 * written in this class's step method.
 * 
 * This frame can be thought of as a "game state update" process. It's goal is
 * to update all game state variables in the data model.
 */
public class MovementFrame extends FrameProcessor {

	private CellGrid grid = game.getGameGrid();
	private ArrayList<CellOccupant> processingQueue = new ArrayList<>();

	public MovementFrame(Game game, SimulationType simulationFrame) {
		super(game, simulationFrame);
	}

	@Override
	public void step(double deltaTimeSeconds) {
		if (processingQueue.size() > 0)
			return;

		Iterator<Map.Entry<String, Cell>> gridIterator = grid
				.getGrid()
				.entrySet()
				.iterator();

		while (gridIterator.hasNext()) {
			Map.Entry<String, Cell> entry = gridIterator.next();
			Cell cell = entry.getValue();

			if (cell.isEmpty())
				continue;

			CellOccupant occupant = cell.getOccupant();

			if (occupant instanceof Bug) {
				Bug bug = (Bug) occupant;
				bug.move();
			}
		}
	}
}

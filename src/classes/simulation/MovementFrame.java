/*
 * @written 3/29/2025
 */
package classes.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import classes.abstracts.Entity;
import classes.abstracts.RunService;
import classes.abstracts.Properties;
import classes.abstracts.Properties.Property;
import classes.entity.CellGrid;
import classes.entity.CellGrid.Cell;
import classes.entity.Game;
import classes.entity.Titan;
import classes.entity.TweenData;
import classes.entity.Vector2;
import classes.settings.GameSettings.SimulationType;
import classes.util.Console;
import classes.util.Math2;

/**
 * This implements the {@code step} method for FrameProcessor. All code that
 * handles what should happen on every Movement step in the simulation should be
 * written in this class's step method.
 * 
 * This frame can be thought of as a "game state update" process. It's goal is
 * to update all game state variables in the data model.
 */
public class MovementFrame extends RunService {

	@SuppressWarnings("unused")
	private Game game = Game.getInstance();

	public MovementFrame(SimulationType simulationFrame) {
		super(simulationFrame);
	}

	@Override
	public void step(double deltaTimeSeconds) {
		CellGrid grid = game.getGameGrid();
		grid.collectCells();

		for (Cell cell : grid.getCells()) {
			Entity<?> entity = cell.getOccupant();

			switch (entity.getProperty(Property.VARIANT, String.class)) {
				case "Titan" -> {
					Titan titan = (Titan) entity;
					// titan.setTarget(grid.getCellWithNearestOccupant(cell).getOccupant());
					// Console.println("Titan target: ", titan.getTarget());
					titan.setTarget(grid.getCellWithNearestOccupant(cell).getOccupant());
					// Console.println("Titan target: ", titan.getTarget());

					ArrayList<Cell> pathCells = grid.getCellPath(cell.getUnit2Center(),
							titan.getTarget().getProperty(Property.POSITION, Vector2.class));

					if (pathCells.size() > 0) {
						Cell c = pathCells.get(1);
						if (c.isAvailable()) {
							titan.assignCell(c);
						}
					}

				}
				default -> {
					ArrayList<Cell> adjCells = grid.getCellsAdjacentTo(cell);
					Cell randCell = grid.getRandomAvailableCellFrom(adjCells);

					if (randCell != null)
						entity.assignCell(randCell);
				}
			}

		}
	}
}

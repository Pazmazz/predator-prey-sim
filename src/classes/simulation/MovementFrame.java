/*
 * @written 3/29/2025
 */
package classes.simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import classes.abstracts.Bug;
import classes.abstracts.Entity;
import classes.abstracts.FrameRunner;
import classes.abstracts.Properties;
import classes.abstracts.Properties.Property;
import classes.entity.Ant;
import classes.entity.CellGrid;
import classes.entity.CellGrid.Cell;
import classes.entity.Game;
import classes.entity.Titan;
import classes.entity.TweenData;
import classes.entity.Vector2;
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
@SuppressWarnings("unused")
public class MovementFrame extends FrameRunner {
	final private Game game = Game.getInstance();

	protected MovementFrame(String processName, double FPS) {
		super(processName, FPS);
	}

	@Override
	public void step(double deltaTimeSeconds) {
		CellGrid grid = game.getGameGrid();
		grid.collectCells();

		for (Cell cell : grid.getGrid().values()) {
			Entity<?> entity = cell.getOccupant();

			if (entity instanceof Bug) {
				Bug<?> bug = (Bug<?>) entity;
				bug.move();
			}
		}

	}
}

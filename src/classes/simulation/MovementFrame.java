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

	public MovementFrame() {
		super(
				settings.getSimulationProcessName(),
				settings.getSimulationFPS(),
				settings.getSimulationDebugInfo());
	}

	@Override
	public void step(double deltaTimeSeconds) {
		CellGrid grid = game.getGameGrid();
		grid.collectCells();

		long currentTime = Time.tick();
		for (Cell cell : grid.getGrid().values()) {
			Entity<?> entity = cell.getOccupant();

			if (entity instanceof Bug) {
				Bug<?> bug = (Bug<?>) entity;
				double movementCooldown = bug.getMovementCooldown();
				long lastMoved = bug.getTimeLastMoved();

				if (currentTime - lastMoved > Time.secondsToNano(movementCooldown)) {
					bug.move();
					bug.setTimeLastMoved();
				}
			}
		}

	}
}

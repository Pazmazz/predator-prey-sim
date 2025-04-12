/*
 * @written 3/29/2025
 */
package classes.simulation;

import java.util.ArrayList;
import java.util.HashMap;

import classes.abstracts.Entity;
import classes.abstracts.RunService;
import classes.abstracts.Properties;
import classes.entity.CellGrid;
import classes.entity.Game;
import classes.entity.TweenData;
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
public class MovementFrame extends RunService {

	@SuppressWarnings("unused")
	private Game game = Game.getInstance();

	public MovementFrame(SimulationType simulationFrame) {
		super(simulationFrame);
	}

	@Override
	public void step(double deltaTimeSeconds) {
		Console.println("running movement");
	}
}

/*
 * @written 3/29/2025
 */
package classes.simulation;

import classes.abstracts.FrameProcessor;
import classes.entity.Game;
import classes.settings.GameSettings.SimulationType;

/**
 * This implements the {@code step} method for FrameProcessor. All code that
 * handles what should happen on every Movement step in the simulation should be
 * written in this class's step method.
 * 
 * This frame can be thought of as a "game state update" process. It's goal is
 * to update all game state variables in the data model.
 */
public class MovementFrame extends FrameProcessor {

	public MovementFrame(Game game, SimulationType simulationFrame) {
		super(game, simulationFrame);
	}

	@Override
	public void step(double deltaTimeSeconds) {
	}
}

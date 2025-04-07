/*
 * @written 3/29/2025
 */
package classes.simulation;

import classes.abstracts.FrameProcessor;
import classes.entity.Game;
import classes.settings.GameSettings.SimulationType;

/**
 * This implements the {@code step} method for FrameProcessor. All code that
 * handles what should happen on every SimulatedLag step in the simulation
 * should be written in this class's step method.
 */
public class SimulatedLagFrame extends FrameProcessor {

	public SimulatedLagFrame(Game game, SimulationType simulationFrame) {
		super(game, simulationFrame);
	}

	@Override
	public void step(double deltaTimeSeconds) {
		// wait(Math.random()*100);
		game.getGameGrid().collectCells();
	}
}

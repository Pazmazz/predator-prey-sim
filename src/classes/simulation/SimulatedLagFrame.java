/*
 * @written 3/29/2025
 */
package classes.simulation;

import classes.abstracts.RunService;
import classes.entity.Game;
import classes.entity.Unit2;
import classes.settings.GameSettings.SimulationType;
import classes.util.Console;

/**
 * This implements the {@code step} method for FrameProcessor. All code that
 * handles what should happen on every SimulatedLag step in the simulation
 * should be written in this class's step method.
 */
public class SimulatedLagFrame extends RunService {

	@SuppressWarnings("unused")
	private Game game = Game.getInstance();

	public SimulatedLagFrame(SimulationType simulationFrame) {
		super(simulationFrame);
	}

	@Override
	public void step(double deltaTimeSeconds) {
		// game.getGameGrid().collectCells();
	}
}

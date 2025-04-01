/*
 * @Written: 3/29/2025
 * 
 * class MovementFrame:
 * 
 * A class that implements the `step()` method for FrameProcessor.
 * All code that handles what should happen on every Movement step
 * in the simulation should be written in this class's step method.
 */

package classes.simulation;

import classes.abstracts.FrameProcessor;
import classes.entity.Game;
import classes.settings.GameSettings.SimulationType;

public class MovementFrame extends FrameProcessor {
	public MovementFrame(Game game, SimulationType simulationFrame) {
		super(game, simulationFrame);
	}

	@Override
	public void step(double deltaTimeSeconds) {
	}
}

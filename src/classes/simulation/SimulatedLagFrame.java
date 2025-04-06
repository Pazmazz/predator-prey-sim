/*
 * @written 3/29/2025
 * 
 * class SimulatedLagFrame:
 * 
 * A class that implements the `step()` method for FrameProcessor.
 * All code that handles what should happen on every SimulatedLagFrame step
 * in the simulation should be written in this class's step method.
 */
package classes.simulation;

import classes.abstracts.FrameProcessor;
import classes.entity.Game;
import classes.settings.GameSettings.SimulationType;

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

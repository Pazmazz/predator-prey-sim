/*
 * @written 3/29/2025
 */
package classes.simulation;

import classes.abstracts.FrameProcessor;
import classes.entity.Game;
import classes.settings.GameSettings.SimulationType;

/**
 * This implements the {@code step} method for FrameProcessor. All code that
 * handles what should happen on every Render step in the simulation should be
 * written in this class's step method.
 */
public class RenderFrame extends FrameProcessor {

    public RenderFrame(Game game, SimulationType simulationFrame) {
        super(game, simulationFrame);
    }

    @Override
    public void step(double deltaTimeSeconds) {
    }
}

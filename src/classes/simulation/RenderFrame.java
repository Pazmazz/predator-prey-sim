/*
 * @written 3/29/2025
 */
package classes.simulation;

import classes.abstracts.FrameRunner;
import classes.entity.CellGrid;
import classes.entity.Game;
import classes.entity.Game.SimulationState;
import classes.settings.GameSettings;
import classes.util.Console;

/**
 * This implements the {@code step} method for FrameProcessor. All code that
 * handles what should happen on every Render step in the simulation should be
 * written in this class's step method.
 */
@SuppressWarnings("unused")
public class RenderFrame extends FrameRunner {

	final private static Game game = Game.getInstance();
	final private static GameSettings settings = game.getSettings();

	public RenderFrame() {
		super(
				settings.getRenderProcessName(),
				settings.getRenderFPS(),
				settings.getRenderDebugInfo());
	}

	@Override
	public void step() {
		CellGrid grid = game.getGameGrid();
		// Console.println(grid.toASCII());
		if (game.getSimulationState() != SimulationState.ENDED) {
			game.getScreen().renderGrid();
		}
	}
}

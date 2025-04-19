/*
 * @written 3/29/2025
 */
package classes.simulation;

import classes.abstracts.FrameRunner;
import classes.entity.CellGrid;
import classes.entity.Game;
import classes.util.Console;

/**
 * This implements the {@code step} method for FrameProcessor. All code that
 * handles what should happen on every Render step in the simulation should be
 * written in this class's step method.
 */
@SuppressWarnings("unused")
public class RenderFrame extends FrameRunner {

	final private Game game = Game.getInstance();

	protected RenderFrame(String processName, double FPS) {
		super(processName, FPS);
	}

	@Override
	public void step(double deltaTimeSeconds) {
		CellGrid grid = game.getGameGrid();
		// Console.println(grid.toASCII());
		game.getScreen().repaintGrid();
	}
}

/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 * 
 * Game entry point file
 */

import classes.abstracts.Application;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.Unit2;
import classes.entity.Vector2;
import classes.util.Console;

public class App extends Application {
	/*
	 * main()
	 * 
	 * Initial entry point for the game application. Responsible for initializing
	 * game UI, game loop, initial logic, and initial game conditions.
	 */
	public static void main(String[] args) {
		Console.setDebugModeEnabled(true);
		// Console.hideDebugPriority(DebugPriority.LOW);
		Console.setConsoleColorsEnabled(false);

		// Game game = new Game();
		// game.start();

		CellGrid grid = new CellGrid(new Unit2(20, 20));

		Cell c0 = grid.getCell(new Unit2(1, 1));
		Cell c1 = grid.getCell(new Unit2(2, 2));
		Cell c2 = grid.getCell(new Unit2(2, 3));

		grid.printCellsAdjacentTo(c1);


	}
}
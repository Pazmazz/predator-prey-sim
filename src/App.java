/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 * 
 * Game entry point file
 */

import classes.abstracts.Application;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.Game;
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

		/*
		 * TEST CODE:
		 * 
		 * Any code below this point is most likely test code -- code
		 * written for debugging or testing out custom classes or
		 * other features.
		 */

		CellGrid grid = new CellGrid(new Unit2(20, 20));

		Console.println(grid.getCell(new Vector2(0.5, 1.1)));
		Console.println(new Vector2(5, 5));
		Console.println(new Unit2(8, 3));
	}
}
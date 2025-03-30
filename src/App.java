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
		Console.setDebugModeEnabled(false);
		Console.setConsoleColorsEnabled(true);

		Game game = new Game();
		game.start();

		CellGrid grid = game.getGameGrid();

		Cell cell1 = grid.getCell(new Vector2(-1, 0));
		Cell cell2 = grid.getCell(new Vector2(-1, 0));
		Cell cell3 = grid.setCell(new Vector2(5, 5));
		Cell cell4 = grid.setCell(new Vector2(-1, -1));

		Console.println(cell1);
		Console.println(cell1 == cell2);

		Console.println(cell3);
		Console.println(cell3.getCellType());

		Console.println(cell4.destroy());

		Console.println(grid.getCell(new Vector2(-1, -1)) == cell4);
	}
}
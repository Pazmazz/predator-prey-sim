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
import classes.entity.IntVector2;
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

		Cell cell1 = grid.getCell(new IntVector2(-1, 0));
		Cell cell2 = grid.getCell(new IntVector2(-1, 0));
		Cell cell3 = grid.setCell(new IntVector2(5, 5));
		Cell cell4 = grid.setCell(new IntVector2(6, 5));
		Cell cell6 = grid.setCell(new IntVector2(5, 6));



		Console.println(cell3.getDirectionRelativeTo(cell4));
	}
}
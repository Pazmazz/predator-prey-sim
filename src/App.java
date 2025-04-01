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
import classes.util.Console.DebugPriority;

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


		Vector2 a = new Vector2(0, 1);
		Console.println(a.subtract(new Vector2(1, 1)));

		Unit2 b = new Unit2(100, 100);
		Console.println(b.subtract(new Unit2(40, 40)));

		// Game game = new Game();
		// game.start();

		// CellGrid grid = new CellGrid(new Unit2(20, 20));

		// Cell cell0 = grid.getCell(new Unit2(2, 1));
		// Cell cell1 = grid.getCell(new Unit2(13, 4));

		// Unit2 a = new Unit2(5, 5);

		// Vector2 b = new Vector2(5.5, 3.5);


		// Cell nextCell = grid.getNextCellPath(cell0, cell1);
		// Console.println(nextCell);

		// nextCell = grid.getNextCellPath(nextCell, cell1);
		// Console.println(nextCell);

		// nextCell = grid.getNextCellPath(nextCell, cell1);
		// Console.println(nextCell);

		// nextCell = grid.getNextCellPath(nextCell, cell1);
		// Console.println(nextCell);

		// nextCell = grid.getNextCellPath(nextCell, cell1);
		// Console.println(nextCell);
	}
}
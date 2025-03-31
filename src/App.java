/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 * 
 * Game entry point file
 */

import classes.abstracts.Application;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.IntVector2;
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
		Console.hideDebugPriority(DebugPriority.LOW);
		Console.setConsoleColorsEnabled(true);

		// Game game = new Game();
		// game.start();

		CellGrid grid = new CellGrid(new IntVector2(10, 10));

		Cell cell0 = grid.getCell(new IntVector2(0, 0));
		
		Cell[] adjCells = grid.getCellsAdjacentTo(cell0);

		for (Cell adjCell : adjCells) {
			Console.println(adjCell);
			Console.println(adjCell.getType());
			Console.println(adjCell.getVacancy());
			Console.println(adjCell.getDirectionRelativeTo(cell0));
			Console.br();
		}
	}
}
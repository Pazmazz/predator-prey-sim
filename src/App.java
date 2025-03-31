/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 * 
 * Game entry point file
 */

import classes.abstracts.Application;
import classes.entity.Ant;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.Game;
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

		Game game = new Game();
		game.start();

		CellGrid grid = game.getGameGrid();
		Cell[] cells = grid.getCellsAdjacentTo(new IntVector2(0, 0));

		for (Cell cell : cells) {
			Console.println(cell);
			Console.println(cell.getType());
			Console.println(cell.getVacancy());
			Console.println(cell.getDirectionRelativeTo(new IntVector2(0, 0)));
			Console.br();
		}

		grid.getCell(new IntVector2(-1, 0)).setOccupant(new Ant());
	}
}
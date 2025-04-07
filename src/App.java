/*
 * @author Alex, Grier, Jaylen, Will
 * @written 3/28/2025
 * 
 * Project VSCode extensions:
 * - Java Extension Pack for Java v0.29.0
 * - vscode-icons
 * 
 * Project code formatter:
 * - Language Support for Java(TM) by Red Hat
 */

import classes.abstracts.Application;
import classes.entity.Ant;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.Unit2;
import classes.util.Console;

/**
 * The entry-point file for the application
 */
public class App extends Application {

	public static void main(String[] args) {
		Console.setDebugModeEnabled(true);
		// Console.hideDebugPriority(DebugPriority.LOW);
		Console.setConsoleColorsEnabled(true);

		// Game game = new Game();
		// game.start();
		//
		// TEST CODE:
		//
		CellGrid grid = new CellGrid(new Unit2(10, 10));

		Cell cell0 = grid.getCell(new Unit2(1, 1));
		Cell cell1 = grid.getCell(new Unit2(1, 2));
		cell1.setOccupant(new Ant());

		Console.println(grid.getCellTopOf(cell0).getOccupant());
		Console.println(grid.getCellRightOf(cell0));

		Console.println(grid.getCellLeftOf(cell0));

		// cell0.moveOccupantTo(cell1);

	}
}

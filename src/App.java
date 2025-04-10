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
 * 
 * TODO:
 * - Make a custom event signal class
 */

import classes.abstracts.Application;
import classes.abstracts.Properties.Property;
import classes.entity.Ant;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.Game;
import classes.entity.Unit2;
import classes.util.Console;
import classes.util.Console.DebugPriority;

/**
 * The entry-point file for the application
 */
public class App extends Application {

	public static void main(String[] args) {
		Console.setDebugModeEnabled(true);
		Console.hideDebugPriority(DebugPriority.LOW);
		// Console.hideDebugPriority(DebugPriority.MEDIUM);
		Console.setConsoleColorsEnabled(false);

		Game game = new Game();
		game.start();

		/*
		 * ---------------
		 * | TEST CODE: |
		 * --------------
		 */

		CellGrid grid = game.getGameGrid();
		Cell cell0 = grid.getCell();
		Cell cell1 = grid.getCell(new Unit2(2, 2));

		Ant ant0 = new Ant(game);
		Ant ant1 = new Ant(game);

		Console.println(ant0, ant1);

		ant0.assignCell(cell1);
		ant1.assignCell(cell0);

		ant1.removeFromCell();
		Console.println(cell0.getOccupant());

		ant0.assignCell(cell0);
		Console.println(cell0.getOccupant());
		Console.println(ant0.getProperty(Property.ASSIGNED_CELL, Cell.class));
		Console.println(cell1.getOccupant());

	}
}

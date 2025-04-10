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

import java.util.HashMap;

import classes.abstracts.Application;
import classes.abstracts.Entity;
import classes.abstracts.Properties.Property;
import classes.entity.Ant;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.Game;
import classes.entity.Unit2;
import classes.util.Console;
import classes.util.Console.DebugPriority;
import interfaces.Callback;
import classes.entity.Doodlebug;

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
		 * --------------
		 * | TEST CODE: |
		 * --------------
		 */

		CellGrid grid = game.getGameGrid();

		Cell cell0 = grid.getCell(new Unit2(1, 1));
		Cell cell1 = grid.getCell(new Unit2(2, 2));

		Ant ant0 = new Ant(game);
		ant0.assignCell(cell0);

		Ant ant1 = new Ant(game);
		ant1.assignCell(cell1);

		Entity<Ant> ant2 = new Ant(game);

		// Thread thread0 = new Thread(() -> {
		// Console.println("Test thread");
		// });

		// thread0.start();

	}
}

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

import java.util.ArrayList;
import java.util.HashMap;

import classes.abstracts.Entity;
import classes.abstracts.FrameProcessor;
import classes.abstracts.Properties.Property;
import classes.entity.Ant;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.CellGrid.GridIntercept;
import classes.entity.Game;
import classes.entity.Unit2;
import classes.util.Console;
import classes.util.Console.DebugPriority;
import interfaces.Callback;
import classes.entity.Doodlebug;
import classes.entity.Vector2;

/**
 * The entry-point file for the application
 */
public class App {

	public static void main(String[] args) {
		Console.setDebugModeEnabled(true);
		// Console.hideDebugPriority(DebugPriority.LOW);
		// Console.hideDebugPriority(DebugPriority.MEDIUM);
		Console.setConsoleColorsEnabled(false);

		Game game = new Game();
		// game.start();

		CellGrid grid = new CellGrid(new Unit2(10, 10));

		Vector2 p0 = new Vector2(0.5, 3.5);
		Vector2 p1 = new Vector2(6.5, 2.5);

		GridIntercept inter = grid.getGridIntercept(p0, p1);

		Console.println(inter);
		/*
		 * --------------
		 * | TEST CODE: |
		 * --------------
		 */

		// thread0.start();

	}
}

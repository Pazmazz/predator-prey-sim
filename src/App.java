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
import classes.abstracts.Bug;
import classes.abstracts.FrameProcessor;
import classes.entity.Ant;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.Game;
import classes.entity.Unit2;
import classes.entity.Vector2;
import classes.util.Console;
import classes.util.Formatter;
import classes.util.Time;
import classes.util.Console.DebugPriority;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

/**
 * The entry-point file for the application
 */
public class App extends Application {

	public static void main(String[] args) {
		Console.setDebugModeEnabled(true);
		// Console.hideDebugPriority(DebugPriority.LOW);
		// Console.hideDebugPriority(DebugPriority.MEDIUM);
		Console.setConsoleColorsEnabled(true);

		Game game = new Game();
		game.start();

		/*
		 * ---------------
		 * | TEST CODE: |
		 * --------------
		 */

	}
}

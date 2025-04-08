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
import classes.abstracts.FrameProcessor;
import classes.abstracts.FrameProcessor.Task;
import classes.abstracts.FrameProcessor.TaskState;
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

/**
 * The entry-point file for the application
 */
public class App extends Application {

	public static void main(String[] args) {
		Console.setDebugModeEnabled(true);
		Console.hideDebugPriority(DebugPriority.LOW);
		Console.hideDebugPriority(DebugPriority.MEDIUM);
		Console.setConsoleColorsEnabled(true);

		Game game = new Game();
		game.start();
		//
		// TEST CODE:
		//
		// Vector2 start = new Vector2(-2, 4);
		// Vector2 end = new Vector2(7, -2);

		CellGrid grid = new CellGrid(new Unit2(10, 10));
		Console.println(new Vector2(3, 6).lerp(new Vector2(10, 12), 0.5));
		// ArrayList<Cell> cells = grid.getCellPath(start, end);

		// for (Cell cell : cells) {
		// Console.println(cell);
		// }

		// Cell cell0 = grid.getCell(new Unit2(1, 1));
		// Cell cell1 = grid.getCell(new Unit2(1, 2));

		// Ant ant0 = new Ant();
		// Ant ant1 = new Ant();

		// cell0.setOccupant(ant0);
		// ant0.assignCell(cell1);
		// ant1.assignCell(cell0);

		// Console.println(cell0.getVacancy());
		// ant1.removeCell();
		// Console.println(cell0.getVacancy());

		// cell0.moveOccupantTo(cell1);

	}
}

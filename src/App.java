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
		Console.setConsoleColorsEnabled(true);

		Game game = new Game();
		game.start();
		//
		// TEST CODE:
		//
		// Vector2 start = new Vector2(-2, 4);
		// Vector2 end = new Vector2(7, -2);

		// CellGrid grid = new CellGrid(new Unit2(10, 10));
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

		// add on one line example
		// game.renderFrame.addTask(
		// new Task("AnimationUpdate", (task) -> {
		// int count = (int) task.get("count");

		// Console.println("Running count: " + count);
		// Console.println("delta: " + Time.nanoToSeconds(task.delta()));
		// Console.br();

		// count++;
		// task.set("count", count);

		// if (count % 61 == 0) {
		// task.suspend(2);
		// }
		// })
		// .suspend(5)
		// .set("position", new Vector2(0, 0))
		// .set("count", 0)
		// .setDuration(10)
		// .setTimeout(8));

	}
}

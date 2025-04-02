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
		Console.setConsoleColorsEnabled(true);

		Game game = new Game();
		game.start();

		CellGrid grid = new CellGrid(new Unit2(20, 20));

		Cell cell0 = grid.getCell(new Unit2(2, 2));
		Cell cell1 = grid.getCell(new Unit2(7, 4));

		Vector2 p0 = cell0.getPosition();
		Vector2 p1 = cell1.getPosition();

		Vector2 direction = p1.subtract(p0);
		Vector2 unit = direction.unit();

		double STEP = (Math.sqrt(2) / 2);
		Console.println("STEP: ", STEP);

		Vector2 nextPoint = p0.add(unit.multiply(STEP * 1));
		Cell nextCell = grid.getCell(nextPoint);
		Console.println(nextCell);

		nextPoint = p0.add(unit.multiply(STEP * 2));
		nextCell = grid.getCell(nextPoint);
		Console.println(nextCell);

		nextPoint = p0.add(unit.multiply(STEP * 3));
		nextCell = grid.getCell(nextPoint);
		Console.println(nextCell);

		nextPoint = p0.add(unit.multiply(STEP * 4));
		nextCell = grid.getCell(nextPoint);
		Console.println(nextCell);
		Console.println(nextPoint);

		nextPoint = p0.add(unit.multiply(STEP * 5));
		nextCell = grid.getCell(nextPoint);
		Console.println(nextCell);
		Console.println(nextPoint);

		nextPoint = p0.add(unit.multiply(STEP * 6));
		nextCell = grid.getCell(nextPoint);
		Console.println(nextCell);
	}
}
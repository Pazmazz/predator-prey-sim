/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 * 
 * Game entry point file
 */

import classes.abstracts.Application;
import classes.entity.Cell;
import classes.entity.CellGrid;
import classes.entity.Unit2;
import classes.entity.Vector2;
import classes.util.Console;
import java.util.Iterator;

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

		// Game game = new Game();
		// game.start();


		/*
		 * TEST CODE:
		 * 
		 * Any code below this point is most likely test code -- code
		 * written for debugging or testing out custom classes or
		 * other features.
		 */

		Vector2 v0 = new Vector2(-2,-2);
		Vector2 v1 = new Vector2(2,2);
		CellGrid grid = new CellGrid(new Unit2(10, 10));

		// Console.println(grid.getGridIntercept(v0, v1).getPointOfIntersection());


		// Console.println(grid.getGridIntercept(v0, v1));
		Iterator<Cell> itr = grid.getCellPathIterator(v0, v1);

		while (itr.hasNext()) {
			Console.println(itr.next());
			Console.br();
		}
		// Console.println(itr.next());
		
		/*
		* y-int = f(ceil(x)) when p.unit() is (+, +) or (+, -)
		* y-int = f(floor(x)) when p.unit() is (-, +) or (-, -)
		* 
		* if floor(f(g(x))) == floor(p.y), then cell = { LEFT, RIGHT } and p_n = (g(x), f(g(x))
		*/

		// Vector2 p_0 = new Vector2(1.19,3.93);
		// Vector2 p_n = new Vector2(-0.52,-1.4);

		// Iterator<Cell> itr = grid.getCellPathIterator(p_0, p_n);


		// CellGrid grid = game.getGameGrid();
		// Cell cell0 = grid.getCell(new Unit2(1, 1));
		// Cell cell1 = grid.getCell(new Unit2(1, 2));

		// Ant ant0 = new Ant();
		// Ant ant1 = new Ant();

		// // Setting an occupant from the cell
		// cell0.setOccupant(ant0);

		// // Setting a cell from the occupant
		// ant1.setCell(cell1);

		// Console.println("$text-yellow Ant 0:");
		// Console.println("Has cell: ", ant0.getCell());

		// Console.println("$text-yellow Ant 1:");
		// Console.println("Has cell: ", ant1.getCell());

		// Console.println("$text-yellow Cell 0");
		// Console.println("Has occupant: ", cell0.getOccupant() != null);

		// Console.println("$text-yellow Cell 1");
		// Console.println("Has occupant: ", cell1.getOccupant() != null);

	}
}
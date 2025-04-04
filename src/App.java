/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 * 
 * Game entry point file
 */

import classes.abstracts.Application;
import classes.entity.CellGrid;
import classes.entity.Unit2;
import classes.entity.Vector2;
import classes.util.Console;

import java.util.ArrayList;
import java.util.Iterator;
import classes.entity.Cell;

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
		Console.setConsoleColorsEnabled(false);

		// Game game = new Game();
		// game.start();

		/*
		 * TEST CODE:
		 * 
		 * Any code below this point is most likely test code -- code
		 * written for debugging or testing out custom classes or
		 * other features.
		 */

		
		/*
		* y-int = f(ceil(x)) when p.unit() is (+, +) or (+, -)
		* y-int = f(floor(x)) when p.unit() is (-, +) or (-, -)
		* 
		* if floor(f(g(x))) == floor(p.y), then cell = { LEFT, RIGHT } and p_n = (g(x), f(g(x))
		*/
		CellGrid grid = new CellGrid(new Unit2(20, 20));

		Vector2 p_0 = new Vector2(-0.49,0);
		Vector2 p_n = new Vector2(3.2, 1.2);

		Iterator<Cell> itr = grid.getCellPathIterator(p_0, p_n);

		Console.println(p_0.signedUnit());
		// ArrayList<Cell> all = grid.getCellPath(p_0, p_n);
		// Console.println(all);

		// itr.next();
		// itr.next();
		// itr.next();
		// itr.next();
		// itr.next();
		// itr.next();

	}
}
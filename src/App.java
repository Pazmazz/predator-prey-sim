/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 * 
 * Game entry point file
 */

import classes.abstracts.Application;
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
		Console.setConsoleColorsEnabled(false);

		// Game game = new Game();
		// game.start();

		// Vector2 a = new Vector2(-1, 0);
		// Vector2 b = new Vector2(0, -1);

		// Console.println(b.subtract(a).unit());


	}
}
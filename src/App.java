/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 * 
 * Game entry point file
 */

import classes.abstracts.Application;
import classes.entity.Cell;
import classes.entity.Game;
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
		Console.setDebugModeEnabled(false);
		Console.setConsoleColorsEnabled(true);

		Game game = new Game();
		game.start();

		game.getGameGrid().setCell(new Vector2(0, 0));
		Cell cell = game.getGameGrid().getCell(new Vector2(0, 0));
		
		Console.println(game.getGameGrid().isInBounds(new Vector2(0, 10)));
		Console.println(cell);
	}
}
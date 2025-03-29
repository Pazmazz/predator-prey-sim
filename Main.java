/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 * 
 * Game entry point file
 */
import classes.AsyncCallback;
import classes.AsyncTask;
import classes.Console;
import classes.Game;
import classes.Game.DebugPriority;

public class Main extends Console {
	/*
	 * main()
	 * 
	 * Initial entry point for the game application. Responsible for initializing
	 * game UI, game loop, initial logic, and initial game conditions.
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.setDebugModeEnabled(true);
		game.start();
	}
}
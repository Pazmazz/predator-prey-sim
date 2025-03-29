/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 * 
 * Game entry point file
 */
import classes.Application;
import classes.AsyncCallback;
import classes.AsyncTask;
import classes.Console;
import classes.Game;
import classes.GameSettings;
import classes.Console.DebugPriority;

public class Main extends Application {
	/*
	 * main()
	 * 
	 * Initial entry point for the game application. Responsible for initializing
	 * game UI, game loop, initial logic, and initial game conditions.
	 */
	public static void main(String[] args) {
		Console.setDebugModeEnabled(true);
		
		Game game = new Game();
		game.start();
	}
}
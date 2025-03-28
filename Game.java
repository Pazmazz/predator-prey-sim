/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 * 
 * Game entry point file
 */
import classes.GameSettings;
import classes.GameScreen;

public class Game implements Runnable {
	private Thread gameThread;
	private GameScreen gameScreen;

	/*
	 * main()
	 * 
	 * Initial entry point for the game application. Responsible for initializing
	 * game UI, game loop, initial logic, and initial game conditions.
	 */
	public static void main(String[] args) {
		Game game = new Game();
		game.start();
	}

	/*
	 * Game():
	 * 
	 * A constructor method for the main Game class to initialize 
	 * prerequisite conditions for the game such as UI components
	 * and the game loop thread.
	 */
	public Game() {
		gameScreen = new GameScreen("Hanson's Hunting Simulator");
		gameThread = new Thread(this);
	}

	/*
	 * start():
	 * 
	 * A no-arg method for running the main game loop thread.
	 */
	public void start() {
		gameThread.start();
	}

	/*
	 * run():
	 * 
	 * A required method override from the `Runnable` interface which
	 * is called once the `gameThread.start()` method is called. 
	 * 
	 * This method serves as the main game loop, which is responsible
	 * for updating game steps, rendering frames, and handling all other
	 * incremental game logic.
	 */
	@Override
	public void run() {
		double deltaTime = 0;

		while (gameThread != null) {
			double gameStepStartTime = tick();
			/*
			 * ORDER OF GAME STEPS:
			 * 
			 * 1) Movement: Calculating the new updated position of all entities on the map.
			 * 2) Render: render all buffered graphics to the screen.
			 */
			movementStep(deltaTime);
			renderStep(deltaTime);

			double gameStepEndTime = tick();
			double gameStepTime = gameStepEndTime - gameStepStartTime;
			double threadYieldTime = GameSettings.FRAME_INTERVAL_MILLISECONDS - gameStepTime;

			if (threadYieldTime > 0) {
				try {
					Thread.sleep((long) threadYieldTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				// Update the delta time between game step intervals
				deltaTime = tick() - gameStepEndTime;
			} else {
				deltaTime = 0;
			}
		}
	}

	public void movementStep(double deltaTime) {
		System.out.println("Computed next movement frame in: " + deltaTime);
	}

	public void renderStep(double deltaTime) {
		System.out.println("Computed next render frame: " + deltaTime);
		// gameScreen.getMasterFrame().repaint();
	}

	public double tick() {
		return System.currentTimeMillis();
	}

	// Getters
	public GameScreen getGameScreen() {
		return gameScreen;
	}
}
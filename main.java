/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 * 
 * Game entry point file
 */
import classes.GameSettings;
import classes.GameScreen;

public class Main implements Runnable {
	private Thread gameThread;

	/*
	 * main()
	 * 
	 * Initial entry point for the game application. Responsible for initializing
	 * game UI, game loop, initial logic, and initial game conditions.
	 */
	public static void main(String[] args) {
		Main game = new Main();
		GameScreen screen = new GameScreen("Hanson's Hunting Simulator");
		
		screen.buildUI();
		game.initGameLoop();
	}

	/*
	 * initGameLoop():
	 * 
	 * A no-arg method for initializing the main game loop thread. This
	 * method creates and runs the main game loop thread at the same
	 * time.
	 */
	public void initGameLoop() {
		gameThread = new Thread(this);
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
			 * 1) Physics: All physics computations such as collision detection are handled.
			 * 2) Movement: Calculating the new updated position of all entities on the map.
			 * 3) Render: render all buffered graphics to the screen.
			 */
			physicsStep(deltaTime);
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

	public void physicsStep(double deltaTime) {
		System.out.println("Computed next physics frame in: " + deltaTime);
	}

	public void movementStep(double deltaTime) {
		System.out.println("Computed next movement frame in: " + deltaTime);
	}

	public void renderStep(double deltaTime) {
		System.out.println("Computed next render frame: " + deltaTime);
	}

	public double tick() {
		return System.currentTimeMillis();
	}
}
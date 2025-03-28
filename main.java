/*
 * @Author(s): Alexander Lance, Grier, Jaylen, Will
 * @Written: 3/28/2025
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
		while (gameThread != null) {
			/*
			 * ORDER OF GAME STEPS:
			 * 
			 * 1) Physics: All physics computations such as collision detection are handled.
			 * 2) Movement: Calculating the new updated position of all entities on the map.
			 * 3) Render: render all buffered graphics to the screen.
			 */
			physicsStep();
			movementStep();
			renderStep();

			try {
				Thread.sleep((long) GameSettings.FRAME_INTERVAL_MILLISECONDS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void physicsStep() {
		System.out.println("Computed physics frame");
	}

	public void movementStep() {
		System.out.println("Computed movement frame");
	}

	public void renderStep() {
		System.out.println("Computed render frame");
	}
}
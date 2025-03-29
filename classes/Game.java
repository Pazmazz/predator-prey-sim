/*
 * @Author(s): Alex, Grier, Jaylen, Will
 * @Written: 3/28/2025
 */
package classes;

public class Game implements Runnable {
	private Thread gameThread;
	private GameScreen gameScreen;
	
	// Update frames
	public MovementFrame movementFrame;

	// All game settings
	public GameSettings settings;
	public double id = Math.random();

	// Internal states
	public static enum GameState {
		INITIAL,
		LOADED,
		RUNNING,
		PAUSED,
		TERMINATED,
	}

	private GameState state = GameState.INITIAL;

	/*
	 * Game():
	 * 
	 * A constructor method for the main Game class to initialize 
	 * prerequisite conditions for the game such as UI components
	 * and the game loop thread.
	 */
	public Game() {
		// IMPORTANT: `settings` must be defined first, since other classes reference it
		settings = new GameSettings();

		gameScreen = new GameScreen(this);
		gameThread = new Thread(this);
		movementFrame = new MovementFrame();

		this.setState(GameState.LOADED);
	}

	/*
	 * start():
	 * 
	 * A no-arg method for running the main game loop thread.
	 */
	public void start() {
		if (isLoaded()) {
			this.setState(GameState.RUNNING);
			gameThread.start();
		} else {
			throw new Error("start() can only be called once per game instance");
		}
	}

	/*
	 * terminate():
	 * 
	 * A no-arg method for terminating the game loop in the current game thread.
	 */
	public void terminate() {
		if (isRunning() || isPaused()) {
			setState(GameState.TERMINATED);
		} else {
			throw new Error("terminate() can only be called if the game is running or paused");
		}
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
	 * 
	 * TODO: Create separate deltaTimes and time steps for frames operating
	 * on different Hz
	 */
	@Override
	public void run() {
		while (isThreadRunning()) {
			// movementFrame.pulse();
			try {
				Thread.sleep(1000);
				System.out.println("Running: " + id);
			} catch (InterruptedException e) {}
		}
	}

	// Getters
	public GameScreen getGameScreen() {
		return gameScreen;
	}

	public GameState getState() {
		return state;
	}

	// Boolean checks
	public boolean isRunning() {
		return state == GameState.RUNNING;
	}

	public boolean isThreadRunning() {
		return isRunning() || isPaused();
	}

	public boolean isInitial() {
		return state == GameState.INITIAL;
	}

	public boolean isLoaded() {
		return state == GameState.LOADED;
	}

	public boolean isPaused() {
		return state == GameState.PAUSED;
	}

	public boolean isTerminated() {
		return state == GameState.TERMINATED;
	}

	// Setters
	public void setState(GameState newState) {
		state = newState;
	}
}
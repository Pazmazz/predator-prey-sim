/*
 * @written 3/29/2025
 */
package classes.abstracts;

import classes.entity.Game;
import classes.settings.GameSettings.SimulationSettings;
import classes.settings.GameSettings.SimulationType;
import classes.util.Console;
import classes.util.Time;

/**
 * An abstract class that provides a {@code pulse} method for updating frame
 * processes in a tight loop. A {@code step} method must be implemented in the
 * subclass which handles what action should occur on that frame.
 */
public abstract class FrameProcessor extends Application {

	private long FPS;
	private long lastPulseTick;
	private long deltaTime = 0;
	private long timeBeforeStep;
	private long timeAfterStep;

	public Game game;
	private SimulationSettings settings;
	private FrameState state = FrameState.RUNNING;

	public enum FrameState {
		RUNNING,
		SUSPENDED,
	}

	protected FrameProcessor(Game game, SimulationType simulationType) {
		this.settings = game.getSettings()
				.getSimulation()
				.getSettings(simulationType);

		this.game = game;
		this.FPS = Time.secondsToNano(settings.getFPS());
		this.lastPulseTick = -Time.secondsToNano(settings.getFPS());
	}

	/**
	 * Unreliably fire the {@code step} method in the current frame. The
	 * {@code step} method will not fire if the time between the last call to
	 * {@code pulse} is less than the minimum allowed interval between frame
	 * steps.
	 *
	 * @return the time it took
	 */
	public long pulse() {
		long preSimulationTime = tick();
		long dt = preSimulationTime - this.lastPulseTick;

		/**
		 * Until we find a better solution to account for unpredictable sleeping
		 * thread durations dipping below the simulation FPS, adding a
		 * millisecond buffer when checking the last frame simulation works for
		 * now.
		 * 
		 * If the delta time (time between last frame simulation step) is less than
		 * the frame FPS, then return -1 (skip this request to run another simulation)
		 */
		if (dt + 1_000_000 < this.FPS)
			return -1;
		else if (this.lastPulseTick < 0)
			dt = 0;

		Console.debugPrint(String.format(
				"$text-%s [%s FRAME] $text-reset ",
				settings.getDebugInfo().getPrimaryColor(),
				settings.getProcessName().toUpperCase()));

		this.lastPulseTick = preSimulationTime;
		this.deltaTime = dt;
		this.timeBeforeStep = preSimulationTime;

		// run the implemented step
		// pre-simulation task binds go here (executePreSimulationTasks())
		step(Time.nanoToSeconds(deltaTime));
		// post-simulation task binds go here (executePostSimulationTasks())

		this.timeAfterStep = tick();
		long simulationTime = this.timeAfterStep - preSimulationTime;

		Console.debugPrint(String.format(
				"completed in: $text-%s %s $text-reset seconds",
				settings.getDebugInfo().getPrimaryColor(),
				Time.nanoToSeconds(simulationTime)));

		Console.debugPrint(String.format(
				"last simulation: $text-%s %s $text-reset seconds ago",
				settings.getDebugInfo().getPrimaryColor(),
				Time.nanoToSeconds(deltaTime)));

		Console.debugPrint("-".repeat(50));
		return simulationTime;
	}

	/**
	 * Gets the most recent delta time between the last simulation interval
	 * 
	 * @return the time between the last simulation interval in seconds
	 * @see #getDeltaTimeSeconds()
	 */
	public double getDeltaTimeSeconds() {
		return Time.nanoToSeconds(this.deltaTime);
	}

	public long timeBeforeStep() {
		return this.timeBeforeStep;
	}

	public long timeAfterStep() {
		return this.timeAfterStep;
	}

	public void suspend() {
		this.state = FrameState.SUSPENDED;
	}

	public void resume() {
		this.state = FrameState.RUNNING;
	}

	public boolean isSuspended() {
		return this.state == FrameState.SUSPENDED;
	}

	public boolean isRunning() {
		return this.state == FrameState.RUNNING;
	}

	protected abstract void step(double deltaTimeSeconds);
}

/*
 * @written 3/29/2025
 */
package classes.abstracts;

import java.util.ArrayList;
import java.util.Iterator;

import classes.entity.Game;
import classes.settings.GameSettings.SimulationSettings;
import classes.settings.GameSettings.SimulationType;
import classes.util.Console;
import classes.util.Time;
import interfaces.Callback;

/**
 * An abstract class that provides a {@code pulse} method for updating frame
 * processes in a tight loop. A {@code step} method must be implemented in the
 * subclass which handles what action should occur on that frame.
 */
public abstract class FrameProcessor extends Application {

	public enum TaskStatus {
		END
	}

	public Game game;
	public long lastPulseTick;
	public long lastDeltaTime = 0;
	public long currentDeltaTime = 0;
	public SimulationSettings settings;
	private ArrayList<Callback> tasks = new ArrayList<>();

	protected FrameProcessor(Game game, SimulationType simulationType) {
		settings = game.getSettings()
				.getSimulation()
				.getSettings(simulationType);

		this.game = game;
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
		long deltaTime = preSimulationTime - lastPulseTick;

		/**
		 * Until we find a better solution to account for unpredictable sleeping
		 * thread durations dipping below the simulation FPS, adding a
		 * millisecond buffer when checking the last frame simulation works for
		 * now.
		 */
		if (deltaTime + 1_000_000 < Time.secondsToNano(settings.getFPS())) {
			return -1;
		} else if (lastPulseTick < 0) {
			deltaTime = 0;
		}

		Console.debugPrint(String.format(
				"$text-%s [%s FRAME] $text-reset",
				settings.getDebugInfo().getPrimaryColor(),
				settings.getProcessName().toUpperCase()));

		//
		// Run the internal step
		//
		this.lastPulseTick = preSimulationTime;
		this.currentDeltaTime = deltaTime;
		step(Time.nanoToSeconds(deltaTime));
		this.lastDeltaTime = deltaTime;
		long simulationTime = tick() - preSimulationTime;

		Console.debugPrint(String.format(
				"completed in: $text-%s %s $text-reset seconds",
				settings.getDebugInfo().getPrimaryColor(),
				Time.nanoToSeconds(simulationTime)));

		Console.debugPrint(String.format(
				"last simulation: $text-%s %s $text-reset seconds ago",
				settings.getDebugInfo().getPrimaryColor(),
				Time.nanoToSeconds(deltaTime)));

		return simulationTime;
	}

	protected double getLastDeltaTimeSeconds() {
		return Time.nanoToSeconds(lastDeltaTime);
	}

	/**
	 * <b>DO NOT USE</b>
	 * <p>
	 * Currently a testing method
	 */
	public void addTask(Callback callback) {
		this.tasks.add(callback);
	}

	/**
	 * <b>DO NOT USE</b>
	 * <p>
	 * Currently a testing method
	 */
	public void executeTasks() {
		Iterator<Callback> taskIterator = tasks.iterator();

		while (taskIterator.hasNext()) {
			Callback task = taskIterator.next();
			TaskStatus status = (TaskStatus) task.call(this.currentDeltaTime);

			if (status == TaskStatus.END) {
				taskIterator.remove();
			}
		}
	}

	protected abstract void step(double deltaTimeSeconds);
}

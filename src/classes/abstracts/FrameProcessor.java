/*
 * @written 3/29/2025
 */
package classes.abstracts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import classes.entity.Game;
import classes.settings.GameSettings.SimulationSettings;
import classes.settings.GameSettings.SimulationType;
import classes.util.Console;
import classes.util.Time;
import classes.util.Console.DebugPriority;
import interfaces.TaskCallback;

/**
 * An abstract class that provides a {@code pulse} method for updating frame
 * processes in a tight loop. A {@code step} method must be implemented in the
 * subclass which handles what action should occur on that frame.
 */
public abstract class FrameProcessor extends Application {

	public Game game;
	public long FPS;
	public long lastPulseTick;
	public long deltaTime = 0;
	public long beforeStep;
	public long afterStep;
	public SimulationSettings settings;
	private ArrayList<Task> tasks = new ArrayList<>();

	public enum TaskState {
		END,
		SUSPENDED,
		RUNNING,
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
				"$text-%s [%s FRAME] $text-reset",
				settings.getDebugInfo().getPrimaryColor(),
				settings.getProcessName().toUpperCase()));

		this.lastPulseTick = preSimulationTime;
		this.deltaTime = dt;
		this.beforeStep = preSimulationTime;

		// run the implemented step
		step(Time.nanoToSeconds(deltaTime));

		/*
		 * If we need more control over when to execute custom tasks, we can call this
		 * method inside the `step` functions and remove it here
		 */
		executeTasks();

		this.afterStep = tick();
		long simulationTime = this.afterStep - preSimulationTime;

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

	/**
	 * Adds a task to the simulation frame's task scheduler
	 * 
	 * @param task the {@code Task} object to be added to the task scheduler
	 * @see #addTask(Task)
	 */
	public void addTask(Task task) {
		this.tasks.add(task);
	}

	/**
	 * Executes all queued tasks in the simulation frame's task pipeline.
	 * 
	 * <p>
	 * This method should ideally be called inside a simulation frame's {@code step}
	 * method, but it may be used outside of this context as well.
	 */
	public void executeTasks() {
		Iterator<Task> taskIterator = tasks.iterator();
		long currentTime = tick();

		while (taskIterator.hasNext()) {
			Task task = taskIterator.next();

			if (task.started() == null)
				task.setStart(currentTime);

			task.setElapsedLifetime(currentTime - task.started());
			if (task.timeout() != -1 && task.elapsedLifetime() > task.timeout()) {
				Console.debugPrint(
						DebugPriority.HIGH,
						String.format("%s timed out", task));

				taskIterator.remove();
			}

			if (task.isSuspended()) {
				if (task.suspendedUntil() == -1)
					task.setSuspendedUntil(currentTime + task.suspended());
				if (currentTime < task.suspendedUntil())
					continue;
				else
					task.resume();
			}

			// task was killed
			if (task.isDead()) {
				Console.debugPrint(
						DebugPriority.HIGH,
						String.format("%s was manually terminated", task));

				taskIterator.remove();
				continue;
			}

			// task exceeded runtime duration
			if (task.duration() != -1 && task.elapsedRuntime() > task.duration()) {
				Console.debugPrint(
						DebugPriority.HIGH,
						String.format("%s fulfilled its runtime duration"));

				task.setState(TaskState.END);
				taskIterator.remove();
				continue;
			}

			task.setDeltaTime(this.deltaTime);
			task.execute();
		}
	}

	/**
	 * The Task class creates a new {@code Task} object which contains metadata
	 * regarding the given task to give the scheduler. It also holds the callback
	 * lambda function for the body of the task.
	 */
	public static class Task {

		final private String name;
		final private HashMap<String, Object> env = new HashMap<>();
		private TaskCallback taskCaller;
		private TaskState state;
		private Long started;

		private long elapsedRuntime = 0;
		private long elapsedLifetime = 0;
		private long deltaTime = 0;
		private long timeout = -1;
		private long suspended = -1;
		private long duration = -1;
		private long suspendedUntil = -1;

		public Task(String name) {
			this.state = TaskState.SUSPENDED;
			this.name = name;
		}

		public Task() {
			this("Anonymous Task");
		}

		public Task(String name, TaskCallback taskCaller) {
			this(name);
			setCallback(taskCaller);
		}

		public Task setCallback(TaskCallback taskCaller) {
			this.taskCaller = taskCaller;
			return this;
		}

		public void execute() {
			this.taskCaller.call(this);
		}

		public Task set(String key, Object value) {
			env.put(key, value);
			return this;
		}

		public Object get(String key) {
			return env.get(key);
		}

		protected Task setElapsedLifetime(long elapsed) {
			this.elapsedLifetime = elapsed;
			return this;
		}

		protected Task setDeltaTime(long lastDelta) {
			this.deltaTime = lastDelta;
			this.elapsedRuntime += lastDelta;
			return this;
		}

		public Task setTimeout(double seconds) {
			this.timeout = Time.secondsToNano(seconds);
			return this;
		}

		public Task suspend(double seconds) {
			if (seconds < 0)
				throw new Error("Task suspension cannot be less than zero");

			this.suspended = Time.secondsToNano(seconds);
			this.state = TaskState.SUSPENDED;
			return this;
		}

		public Task setDuration(double duration) {
			if (duration < 0)
				throw new Error("Task duration cannot be less than zero");

			this.duration = Time.secondsToNano(duration);
			return this;
		}

		protected Task setStart(Long started) {
			this.started = started;
			return this;
		}

		protected Task setSuspendedUntil(long suspendedUntil) {
			this.suspendedUntil = suspendedUntil;
			return this;
		}

		protected Task setState(TaskState state) {
			this.state = state;
			return this;
		}

		public void resume() {
			this.state = TaskState.RUNNING;
			this.suspended = -1;
			this.suspendedUntil = -1;
		}

		public Long started() {
			return this.started;
		}

		public long elapsedRuntime() {
			return this.elapsedRuntime;
		}

		public long elapsedLifetime() {
			return this.elapsedLifetime;
		}

		public long delta() {
			return this.deltaTime;
		}

		public long duration() {
			return this.duration;
		}

		public long suspended() {
			return this.suspended;
		}

		public long suspendedUntil() {
			return this.suspendedUntil;
		}

		public long timeout() {
			return this.timeout;
		}

		public String name() {
			return this.name;
		}

		public void kill() {
			this.state = TaskState.END;
		}

		public boolean isRunning() {
			return this.state == TaskState.RUNNING;
		}

		public boolean isSuspended() {
			return this.state == TaskState.SUSPENDED
					&& this.suspended != -1;
		}

		public boolean isDead() {
			return this.state == TaskState.END;
		}

		@Override
		public String toString() {
			return String.format(
					"$text-cyan Task$text-reset <$text-yellow %s$text-reset >",
					this.name);
		}
	}

	protected abstract void step(double deltaTimeSeconds);
}

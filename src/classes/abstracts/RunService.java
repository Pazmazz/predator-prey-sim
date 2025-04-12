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
import classes.util.Console.DebugPriority;
import classes.util.Time;
import interfaces.TaskCallback;

/**
 * An abstract class that provides a {@code pulse} method for updating frame
 * processes in a tight loop. A {@code step} method must be implemented in the
 * subclass which handles what action should occur on that frame.
 */
public abstract class RunService {

	private Game game = Game.getInstance();

	private long FPS;
	private long lastPulseTick;
	private long deltaTime = 0;
	private long timeBeforeStep;
	private long timeAfterStep;

	private SimulationSettings settings;
	private FrameState state = FrameState.RUNNING;
	private ArrayList<Task> preSimulationTasks = new ArrayList<>();
	private ArrayList<Task> postSimulationTasks = new ArrayList<>();

	public static FrameState masterState = FrameState.RUNNING;

	public enum FrameState {
		RUNNING,
		SUSPENDED,
	}

	protected RunService(SimulationType simulationType) {
		this.settings = game.getSettings()
				.getSimulation()
				.getSettings(simulationType);

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
		long preSimulationTime = Time.tick();
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
		executeTasks(this.preSimulationTasks);
		step(Time.nanoToSeconds(deltaTime));
		executeTasks(this.postSimulationTasks);
		// post-simulation task binds go here (executePostSimulationTasks())

		this.timeAfterStep = Time.tick();
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

	// TODO: Add documentation
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

	public Task onPreSimulation(TaskCallback taskCaller) {
		Task task = new Task(taskCaller);
		this.preSimulationTasks.add(task);
		return task;
	}

	public Task onPostSimulation(TaskCallback taskCaller) {
		Task task = new Task(taskCaller);
		this.postSimulationTasks.add(task);
		return task;
	}

	public static void suspendAll() {
		masterState = FrameState.SUSPENDED;
	}

	public static void resumeAll() {
		masterState = FrameState.RUNNING;
	}

	public static boolean isAllSuspended() {
		return masterState == FrameState.SUSPENDED;
	}

	public static boolean isAllRunning() {
		return masterState == FrameState.RUNNING;
	}

	/**
	 * Executes all queued tasks in the simulation frame's task pipeline.
	 * 
	 * <p>
	 * This method should ideally be called inside a simulation frame's {@code step}
	 * method, but it may be used outside of this context as well.
	 */
	public void executeTasks(ArrayList<Task> tasks) {
		if (tasks.size() == 0)
			return;

		Iterator<Task> taskIterator = tasks.iterator();
		long currentTime = Time.tick();

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

		// TODO: Add documentation
		public Task(String name) {
			this.state = TaskState.SUSPENDED;
			this.name = name;
		}

		public Task() {
			this("Anonymous");
		}

		public Task(TaskCallback taskCaller) {
			this("Anonymous", taskCaller);
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

		public void pause() {
			this.state = TaskState.SUSPENDED;
			this.suspendedUntil = (long) Double.POSITIVE_INFINITY;
			this.suspended = (long) Double.POSITIVE_INFINITY;
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

	public enum TaskState {
		END,
		SUSPENDED,
		RUNNING,
	}

	protected abstract void step(double deltaTimeSeconds);
}

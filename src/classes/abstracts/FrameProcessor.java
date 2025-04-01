/*
 * @Written: 3/29/2025
 * 
 * abstract class FrameProcessor:
 * 
 * An abstract class that provides a `pulse()` method for updating
 * frame processes in a tight loop. A `step()` method must be
 * implemented in the subclass which handles what action should
 * occur on that frame.
 */

package classes.abstracts;

import classes.entity.Game;
import classes.settings.GameSettings.SimulationSettings;
import classes.settings.GameSettings.SimulationType;
import classes.util.Console;
import classes.util.Time;

public abstract class FrameProcessor extends Application {
	public Game game;
	public long lastPulseTick;
	public long lastDeltaTime = 0;
	public SimulationSettings settings;

	protected FrameProcessor(Game game, SimulationType simulationType) {
		settings = game.getSettings()
			.getSimulation()
			.getSettings(simulationType);
			
		this.game = game;
		this.lastPulseTick = -Time.secondsToNano(settings.getFPS());
	}

	public long pulse() {
		long preSimulationTime = tick();
		long deltaTime = preSimulationTime - lastPulseTick;

		/*
		 * deltaTime + 1_000_000:
		 * 
		 * Until we find a better solution to account for unpredictable
		 * sleeping thread durations dipping below the simulation FPS,
		 * adding a millisecond buffer when checking the last frame
		 * simulation works for now.
		 */
		if (deltaTime + 1_000_000 < Time.secondsToNano(settings.getFPS())) {
			return -1;
		} else if (lastPulseTick < 0) {
			deltaTime = 0;
		}

		Console.debugPrint(
			"$text-%s [%s FRAME] $text-reset"
				.formatted(
					settings.getDebugInfo().getPrimaryColor(),
					settings.getProcessName().toUpperCase()
				)
		);

		lastPulseTick = preSimulationTime;
		step(Time.nanoToSeconds(deltaTime));
		lastDeltaTime = deltaTime;
		long simulationTime = tick() - preSimulationTime;

		Console.debugPrint(
			"completed in: $text-%s %s $text-reset seconds"
				.formatted(
					settings.getDebugInfo().getPrimaryColor(),
					Time.nanoToSeconds(simulationTime)
				)
		);

		Console.debugPrint(
			"last simulation: $text-%s %s $text-reset seconds ago"
				.formatted(settings.getDebugInfo().getPrimaryColor(),
					Time.nanoToSeconds(deltaTime)
				)
		);

		return simulationTime;
	}

	protected double getLastDeltaTimeSeconds() {
		return Time.nanoToSeconds(lastDeltaTime);
	}

	protected long step() {
		long preStep = tick();
		step(settings.getFPS());
		long simulationStep = tick() - preStep;
		
		lastDeltaTime = simulationStep;
		return simulationStep;
	}

	protected abstract void step(double deltaTimeSeconds);
}

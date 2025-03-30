/*
 * All configurable game settings
 */

package classes.settings;
import classes.abstracts.FrameProcessor.SimulationType;
import java.util.HashMap;

public class GameSettings {
	// Text (immutable at runtime)
	final private String gameHeaderText;
	final private String gameTitle;
	
	// Render settings (immutable at runtime)
	final private int cellSize; // Pixels
	final private int gridSizeX;
	final private int gridSizeY;

	final private int screenWidth; 	// Pixels
	final private int screenHeight; // Pixels

	public GameSettings() {
		gameHeaderText = "one";
		gameTitle = "two";

		cellSize = 48;

		gridSizeX = 10;
		gridSizeY = 10;

		screenWidth = gridSizeX * cellSize;
		screenHeight = gridSizeY * cellSize;

		simulationInfo = new SimulationInfo();
	}

	// Render settings (mutable at runtime)
	final private SimulationInfo simulationInfo;

	public class DebugInfo {
		private String primaryColor;
		private String secondaryColor;

		public DebugInfo() {
			primaryColor = "green";
			secondaryColor = "cyan";
		}

		public String getPrimaryColor() {
			return primaryColor;
		}

		public String getSecondaryColor() {
			return secondaryColor;
		}

		public DebugInfo setPrimaryColor(String color) {
			primaryColor = color;
			return this;
		}

		public DebugInfo setSecondaryColor(String color) {
			secondaryColor = color;
			return this;
		}
	}

	public class SimulationInfo {
		final private double FPS = 1.0 / 60;
		final private HashMap<SimulationType, SimulationSettings> settings;

		public SimulationInfo() {
			settings = new HashMap<>();

			SimulationSettings render = new SimulationSettings()
				.setFPS(1.0 / 60)
				.setProcessName("Render");
				
			render.getDebugInfo().setPrimaryColor("red");

			SimulationSettings movement = new SimulationSettings()
				.setFPS(1.0)
				.setProcessName("Movement");

			movement.getDebugInfo().setPrimaryColor("yellow");

			SimulationSettings simulatedLag = new SimulationSettings()
				.setFPS(1.0 / 60)
				.setProcessName("SimulatedLag");

			simulatedLag.getDebugInfo().setPrimaryColor("purple");
			
			settings.put(SimulationType.RENDER, render);
			settings.put(SimulationType.MOVEMENT, movement);
			settings.put(SimulationType.SIMULATED_LAG, simulatedLag);
		}

		public double getFPS() {
			return FPS;
		}

		public SimulationSettings getSettings(SimulationType simulationType) {
			return settings.get(simulationType);
		}
	}

	public class SimulationSettings {
		private double FPS;
		private String processName;
		final private DebugInfo debugInfo;

		public SimulationSettings() {
			debugInfo = new DebugInfo();
		}

		public String getProcessName() {
			return processName;
		}

		public double getFPS() {
			return FPS;
		}

		public DebugInfo getDebugInfo() {
			return debugInfo;
		}

		public SimulationSettings setFPS(double FPS) {
			this.FPS = FPS;
			return this;
		}

		public SimulationSettings setProcessName(String name) {
			processName = name;
			return this;
		}
	}

	public SimulationInfo getSimulation() {
		return simulationInfo;
	}

	public String getTitle() {
		return gameTitle;
	}

	public String getHeaderText() {
		return gameHeaderText;
	}

	public int getCellSize() {
		return cellSize;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}
}

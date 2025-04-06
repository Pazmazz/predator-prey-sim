/*
 * All configurable game settings
 */

package classes.settings;

import classes.entity.Unit2;
import java.util.HashMap;

public class GameSettings {
	/* ============ */
	/* CLASS FIELDS */
	/* ============ */
	final private String gameHeaderText;
	final private String gameTitle;
	
	final private int cellSize;
	final private Unit2 gridSize;

	final private int screenWidth;
	final private int screenHeight;

	final private SimulationInfo simulationInfo;

	/* =========== */
	/* CONSTRUCTOR */
	/* =========== */
	public GameSettings() {
		/* Editable */
		gameHeaderText = "one"; // Text of the main header within the window
		gameTitle = "two"; // Title of game window

		cellSize = 36; // Pixels
		gridSize = new Unit2(20, 20); // Cell units 

		/* Non-Editable */
		screenWidth = gridSize.getX() * cellSize;
		screenHeight = gridSize.getY() * cellSize;

		simulationInfo = new SimulationInfo();
	}

	/* ===== */
	/* ENUMS */
	/* ===== */
	public enum SimulationType {
		MOVEMENT,
		RENDER,
		SIMULATED_LAG,
	}

	/* ==================== */
	/* SETTINGS API CLASSES */
	/* ==================== */
	/*
	 * class DebugInfo:
	 * 
	 * A simple API for getting and setting debug info
	 * variables. For now, the sole purpose of this class
	 * is to get and set debug message colors
	 */
	public class DebugInfo {
		private String primaryColor;
		private String secondaryColor;

		public DebugInfo() {
			/* Editable */
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

	/*
	 * class SimulationInfo:
	 * 
	 * This class is the main API for accessing simulation
	 * settings such as FPS. It holds all information about
	 * each simulation frame and can be accessed with chained
	 * getter methods, for example:
	 * 
	 * ```
	 * SimulationInfo info = new SimulationInfo();
	 * 
	 * info.getFPS();
	 * info.getSettings(simulationType).getDebugInfo();
	 * ```
	 * 
	 * This class aggregates instances of `SimulationSettings`
	 */
	public class SimulationInfo {
		final private double FPS;
		final private HashMap<SimulationType, SimulationSettings> settings;

		public SimulationInfo() {
			/* Editable */
			FPS = 1.0 / 60;
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
					.setFPS(5)
					.setProcessName("SimulatedLag");

			simulatedLag.getDebugInfo().setPrimaryColor("purple");

			/* Non-Editable */
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

	/*
	 * class SimulationSettings:
	 * 
	 * A settings class that holds all relevant information
	 * about a given simulation process. These instances are
	 * aggregated in the Simulation class for central organization.
	 */
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

	/* ================= */
	/* GAME SETTINGS API */
	/* ================= */
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

	public Unit2 getGridSize() {
		return gridSize;
	}
}

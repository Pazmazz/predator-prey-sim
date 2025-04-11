/*
 * All configurable game settings
 */
package classes.settings;

import classes.entity.Unit2;
import java.util.HashMap;

public class GameSettings {

	final private String gameHeaderText;
	final private String gameTitle;

	final private int cellSize;
	final private Unit2 gridSize;
	final private int initialAnts;
	final private int initialDoodlebugs;

	final private int screenWidth;
	final private int screenHeight;
	final private SimulationInfo simulationInfo;

	public GameSettings() {
		//
		// Editable
		//
		this.gameHeaderText = "one"; // Text of the main header within the window
		this.gameTitle = "two"; // Title of game window

		this.cellSize = 36; // Pixels
		this.gridSize = new Unit2(20, 20); // Cell units
		this.initialAnts = 100;
		this.initialDoodlebugs = 5;

		//
		// Non-Editable
		//
		this.screenWidth = gridSize.getX() * this.cellSize;
		this.screenHeight = gridSize.getY() * this.cellSize;
		this.simulationInfo = new SimulationInfo();
	}

	public enum SimulationType {
		MOVEMENT,
		RENDER,
		SIMULATED_LAG,
	}

	public class DebugInfo {

		private String primaryColor;
		private String secondaryColor;

		public DebugInfo() {
			//
			// Editable
			//
			this.primaryColor = "green";
			this.secondaryColor = "cyan";
		}

		public String getPrimaryColor() {
			return this.primaryColor;
		}

		public String getSecondaryColor() {
			return this.secondaryColor;
		}

		public DebugInfo setPrimaryColor(String color) {
			this.primaryColor = color;
			return this;
		}

		public DebugInfo setSecondaryColor(String color) {
			this.secondaryColor = color;
			return this;
		}
	}

	/**
	 * This class is the main API for accessing simulation settings such as FPS.
	 * It holds all information about each simulation frame and can be accessed
	 * with chained getter methods
	 *
	 * <p>
	 * This class aggregates instances of `SimulationSettings`
	 */
	public class SimulationInfo {

		final private double FPS;
		final private HashMap<SimulationType, SimulationSettings> settings;

		public SimulationInfo() {
			//
			// Editable
			//
			FPS = 1.0 / 60;
			settings = new HashMap<>();

			// Render
			SimulationSettings render = new SimulationSettings()
					.setFPS(1.0 / 60)
					.setProcessName("Render");

			// Movement
			SimulationSettings movement = new SimulationSettings()
					.setFPS(1.0)
					.setProcessName("Movement");

			// SimulatedLag
			SimulationSettings simulatedLag = new SimulationSettings()
					.setFPS(3.0)
					.setProcessName("SimulatedLag");

			render.getDebugInfo().setPrimaryColor("red");
			movement.getDebugInfo().setPrimaryColor("yellow");
			simulatedLag.getDebugInfo().setPrimaryColor("purple");

			//
			// Non-Editable
			//
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

	/**
	 * A settings class that holds all relevant information about a given
	 * simulation process. These instances are aggregated in the Simulation
	 * class for central organization.
	 */
	public class SimulationSettings {

		private double FPS;
		private String processName;
		final private DebugInfo debugInfo;

		public SimulationSettings() {
			this.debugInfo = new DebugInfo();
		}

		public String getProcessName() {
			return this.processName;
		}

		public double getFPS() {
			return FPS;
		}

		public DebugInfo getDebugInfo() {
			return this.debugInfo;
		}

		public SimulationSettings setFPS(double FPS) {
			this.FPS = FPS;
			return this;
		}

		public SimulationSettings setProcessName(String name) {
			this.processName = name;
			return this;
		}
	}

	//
	// Public getters
	//
	public SimulationInfo getSimulation() {
		return this.simulationInfo;
	}

	public String getTitle() {
		return this.gameTitle;
	}

	public String getHeaderText() {
		return this.gameHeaderText;
	}

	public int getCellSize() {
		return this.cellSize;
	}

	public int getScreenWidth() {
		return this.screenWidth;
	}

	public int getScreenHeight() {
		return this.screenHeight;
	}

	public Unit2 getGridSize() {
		return this.gridSize;
	}

	public int getInitialAnts() {
		return this.initialAnts;
	}

	public int getInitialDoodlebugs() {
		return this.initialDoodlebugs;
	}
}

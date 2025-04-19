/*
 * All configurable game settings
 */
package classes.settings;

import classes.entity.Unit2;
import classes.util.Console;
import classes.util.Console.DebugPriority;

import java.util.HashMap;

public class GameSettings {

	final private String gameHeaderText;
	final private String gameTitle;

	private int cellSize;
	private Unit2 gridSize;
	private int initialAnts;
	private int initialDoodlebugs;
	private int gridLineThickness;

	private boolean antBreedingEnabled;
	private boolean doodlebugBreedingEnabled;

	final private int screenWidth;
	final private int screenHeight;
	final private int screenAspectRatio;
	final private SimulationInfo simulationInfo;

	public GameSettings() {
		//
		// Editable
		//
		this.gameHeaderText = "one"; // Text of the main header within the window
		this.gameTitle = "two"; // Title of game window

		// Game grid
		this.screenWidth = 560;
		this.screenHeight = 680;

		this.gridLineThickness = 2;
		this.gridSize = new Unit2(20, 20);

		this.initialAnts = 100;
		this.initialDoodlebugs = 5;

		this.antBreedingEnabled = true;
		this.doodlebugBreedingEnabled = true;

		// Debug
		Console.setDebugModeEnabled(true);
		Console.setConsoleColorsEnabled(false);
		Console.hideDebugPriority(DebugPriority.LOW);
		Console.hideDebugPriority(DebugPriority.MEDIUM);

		//
		// Non-Editable
		//
		this.simulationInfo = new SimulationInfo();
		this.screenAspectRatio = this.screenWidth / this.screenHeight;
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

		private double FPS;
		final private HashMap<SimulationType, SimulationSettings> settings;

		public SimulationInfo() {
			//
			// Editable
			//
			FPS = 1.0 / 60;
			settings = new HashMap<>();

			// (vv) Note: Set FPS to 0 if you want it to match the game FPS (vv) //

			// Render
			SimulationSettings render = new SimulationSettings()
					.setFPS(0)
					.setProcessName("Render");

			// Movement
			SimulationSettings movement = new SimulationSettings()
					.setFPS(0)
					.setProcessName("Movement");

			// SimulatedLag
			SimulationSettings simulatedLag = new SimulationSettings()
					.setFPS(0)
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

		public void setFPS(double FPS) {
			this.FPS = FPS;
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

	public int getGridLineThickness() {
		return this.gridLineThickness;
	}

	public boolean getAntBreedingEnabled() {
		return this.antBreedingEnabled;
	}

	public boolean getDoodlebugBreedingEnabled() {
		return this.doodlebugBreedingEnabled;
	}

	public int getScreenAspectRatio() {
		return screenAspectRatio;
	}

	//
	// Public setters
	//
	public void setInitialAnts(int initialAnts) {
		this.initialAnts = initialAnts;
	}

	public void setInitialDoodlebugs(int initialDoodlebugs) {
		this.initialDoodlebugs = initialDoodlebugs;
	}

	public void setGridSize(Unit2 gridSize) {
		this.gridSize = gridSize;
	}

	public void setGridLineThickness(int gridLineThickness) {
		this.gridLineThickness = gridLineThickness;
	}

	public void setAntBreedingEnabled(boolean bool) {
		this.antBreedingEnabled = bool;
	}

	public void setDoodlebugBreedingEnabled(boolean bool) {
		this.doodlebugBreedingEnabled = bool;
	}
}

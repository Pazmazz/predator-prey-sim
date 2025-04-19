/*
 * All configurable game settings
 */
package classes.settings;

import classes.entity.DebugInfo;
import classes.entity.Unit2;
import classes.util.Console;
import classes.util.Console.DebugPriority;

import java.util.HashMap;

@SuppressWarnings("unused")
public class GameSettings {

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

		// RunService
		this.gameHertz = 1.0 / 60;
		this.renderFPS = 0;
		this.simulationFPS = 0;

		this.renderDebugInfo = new DebugInfo()
				.setPrimaryColor("red");

		this.simulationDebugInfo = new DebugInfo()
				.setPrimaryColor("yellow");

		// Debug
		Console.setDebugModeEnabled(true);
		Console.setConsoleColorsEnabled(false);
		Console.hideDebugPriority(DebugPriority.LOW);
		Console.hideDebugPriority(DebugPriority.MEDIUM);

		//
		// Non-Editable
		//
		this.screenAspectRatio = this.screenWidth / this.screenHeight;
	}

	final private String gameHeaderText;
	final private String gameTitle;

	private int cellSize;
	private Unit2 gridSize;
	private int initialAnts;
	private int initialDoodlebugs;
	private int gridLineThickness;

	private double gameHertz;
	private double renderFPS;
	private double simulationFPS;

	private DebugInfo renderDebugInfo;
	private DebugInfo simulationDebugInfo;

	private boolean antBreedingEnabled;
	private boolean doodlebugBreedingEnabled;

	final private int screenWidth;
	final private int screenHeight;
	final private int screenAspectRatio;

	//
	// Public getters
	//
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

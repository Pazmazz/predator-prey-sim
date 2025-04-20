/*
 * All configurable game settings
 */
package classes.settings;

import classes.entity.DebugInfo;
import classes.entity.Unit2;
import classes.util.Console;
import classes.util.Math2;
import classes.util.Console.DebugPriority;

import java.awt.Color;
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
		this.screenWidth = 600;
		// this.screenHeight = 680;

		this.gridLineThickness = 2;
		this.gridBorderPadding = 1;
		this.gridSize = new Unit2(20, 20);

		this.gridBackgroundColor = Color.BLACK;
		this.gridLinesColor = Color.BLUE;

		this.initialAnts = 100;
		this.initialDoodlebugs = 5;
		this.antMovementCooldown = 1.0 / 10;
		this.doodlebugMovementCooldown = 1.0 / 10;

		this.antBreedingEnabled = true;
		this.doodlebugBreedingEnabled = true;

		// RunService
		this.gameHertz = 1.0 / 60;

		this.renderFPS = 0;
		this.renderProcessName = "Render";
		this.renderDebugInfo = new DebugInfo();
		this.renderDebugInfo.setPrimaryColor("red");

		this.simulationFPS = 0;
		this.simulationProcessName = "Simulation";
		this.simulationDebugInfo = new DebugInfo();
		this.renderDebugInfo.setPrimaryColor("yellow");

		// Debug
		Console.setDebugModeEnabled(true);
		Console.setConsoleColorsEnabled(true);
		Console.hideDebugPriority(DebugPriority.LOW);
		Console.hideDebugPriority(DebugPriority.MEDIUM);

		//
		// Non-Editable
		//
		this.screenHeight = this.screenWidth + 120;
		this.screenAspectRatio = this.screenWidth / this.screenHeight;
	}

	final private String gameHeaderText;
	final private String gameTitle;

	private int cellSize;
	private Unit2 gridSize;
	private int initialAnts;
	private int initialDoodlebugs;
	private int gridLineThickness;
	private int gridBorderPadding;
	private Color gridBackgroundColor;
	private Color gridLinesColor;
	private double antMovementCooldown;
	private double doodlebugMovementCooldown;

	private String[] bugFirstNames = new String[] {
			"Anton",
			"Andy",
			"Anya",
			"Antoinette",
			"Anthony",
			"Pip",
			"Dot",
			"Tiny",
			"Bitsy",
			"Nib",
			"Flick",
			"Chip",
			"Scurry",
			"Twitch",
			"Minnie",
			"Zippy",
			"Clove",
			"Buzz",
			"Pebble",
			"Midge",
			"Skitter",
			"Linty",
			"Tilly",
			"Crumb",
			"Tizzy",
			"Sarge",
			"Queenie",
			"Thorax",
			"Mandee",
			"Scout",
			"Rook",
			"Hexa",
			"Bubbles",
			"Coco",
			"Luma",
			"Amber",
			"Inky",
			"Soot",
			"Grain",
			"Bean",
			"Noodle",
			"Speck",
			"Zula",
			"Wisp",
			"Rolo",
			"Blip",
			"Elba",
			"Gnatash",
			"Antsy",
			"Scamp",
	};

	private String[] bugLastNames = new String[] {
			"Smurf",
			"Crick",
			"Underleaf",
			"Tunneler",
			"Sprocket",
			"Dirtchaser",
			"Burrowes",
			"Mulch",
			"Leafson",
			"Grubbe",
			"Stickley",
			"Antwhistle",
			"Skitteridg",
			"Buggins",
			"Pollen",
			"Pebbleston",
			"Webber",
			"Groundling",
			"Scritch",
			"Sapthorn",
			"Nestlewick",
			"Rootwell",
			"Scuttle",
			"Chitter",
			"Mossgrove",
			"Whiskerfel",
			"Dapple",
			"Clickley",
			"Hivetide",
			"Dungworth",
			"Crawleigh",
			"Barkley",
			"Thistledew",
			"Crumbwell",
			"Swarmson",
			"Fuzzle",
			"Scampers",
			"Holloway",
			"Beetlewitz",
			"Twigg",
			"Latchwing",
			"Fizzleburr",
			"Dapplewick",
			"Antlersnap",
			"Snickett",
			"Wriggles",
			"Dapplethor",
			"Chirple",
			"Minibeet",
			"Creever",
			"Dustmoor",
	};

	private double gameHertz;
	private double renderFPS;
	private double simulationFPS;

	private DebugInfo renderDebugInfo;
	private DebugInfo simulationDebugInfo;

	private String renderProcessName;
	private String simulationProcessName;

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
		return this.screenAspectRatio;
	}

	public DebugInfo getRenderDebugInfo() {
		return this.renderDebugInfo;
	}

	public double getRenderFPS() {
		return this.renderFPS;
	}

	public String getRenderProcessName() {
		return this.renderProcessName;
	}

	public DebugInfo getSimulationDebugInfo() {
		return this.simulationDebugInfo;
	}

	public double getSimulationFPS() {
		return this.simulationFPS;
	}

	public String getSimulationProcessName() {
		return this.simulationProcessName;
	}

	public double getGameHertz() {
		return this.gameHertz;
	}

	public int getGridBorderPadding() {
		return this.gridBorderPadding;
	}

	public Color getGridBackgroundColor() {
		return this.gridBackgroundColor;
	}

	public Color getGridLinesColor() {
		return this.gridLinesColor;
	}

	public double getAntMovementCooldown() {
		return this.antMovementCooldown;
	}

	public double getDoodlebugMovementCooldown() {
		return this.doodlebugMovementCooldown;
	}

	public String[] getBugFirstNames() {
		return this.bugFirstNames;
	}

	public String[] getBugLastNames() {
		return this.bugLastNames;
	}

	public String getRandomBugFirstName() {
		return this.bugFirstNames[Math2.randInt(this.bugFirstNames.length)];
	}

	public String getRandomBugLastName() {
		return this.bugLastNames[Math2.randInt(this.bugLastNames.length)];
	}

	public String getRandomBugFirstAndLastName() {
		return new StringBuilder(this.getRandomBugFirstName())
				.append(" ")
				.append(this.getRandomBugLastName())
				.toString();
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

	public void setAntMovementCooldown(double cooldown) {
		this.antMovementCooldown = cooldown;
	}

	public void setDoodlebugMovementCooldown(double cooldown) {
		this.doodlebugMovementCooldown = cooldown;
	}
}

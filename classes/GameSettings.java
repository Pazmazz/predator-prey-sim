/*
 * All configurable game settings
 */

package classes;

import java.util.HashMap;
import java.util.Map;

import classes.Console.DebugPriority;

public class GameSettings {
	/* -------- */
	/* Editable */
	/* -------- */

	// Text (immutable at runtime)
	final public static String GAME_HEADER_TITLE = "Bug sim";
	final public static String GAME_TITLE = "To be determined";

	// Debug info (immutable at runtime)
	final public static Map<DebugPriority, String> debugPrefixes = new HashMap<>() {{
		put(DebugPriority.LOW, "[Debug Low]");
		put(DebugPriority.MEDIUM, "[Debug Medium]");
		put(DebugPriority.HIGH, "[Debug High]");
	}};

	// Render settings (mutable at runtime)
	public double simulationFPS = 1.0 / 2; // Game simulations per second
	public double renderFPS = 1.0 / 60; // Render frames per second
	public int movementFPS = 1; // Movements per second

	// Render settings (immutable at runtime)
	final public static int CELL_SIZE = 48; // Pixels

	final public static int GRID_SIZE_X = 10;	// Cell multiplier
	final public static int GRID_SIZE_Y = 10;	// Cell multiplier

	/* ------------ */
	/* Non-Editable */
	/* ------------ */
	final public int SCREEN_WIDTH = GRID_SIZE_X * CELL_SIZE; 	// Pixels
	final public int SCREEN_HEIGHT = GRID_SIZE_Y * CELL_SIZE;	// Pixels

	final public double RENDER_INTERVAL_MILLISECONDS = renderFPS * 1000; // Milliseconds between render frames
	final public double MOVEMENT_INTERVAL_MILLISECONDS = movementFPS * 1000; // Milliseconds between movement frames
	final public double SIMULATION_INTERVAL_MILLISECONDS = simulationFPS * 1000; // Milliseconds between simulation frames
}

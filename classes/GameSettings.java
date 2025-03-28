/*
 * All configurable game settings
 */

package classes;

public class GameSettings {
	/* -------- */
	/* Editable */
	/* -------- */

	// Render settings
	final public static int FPS = 2; // Render frames per second
	final public static int CELL_SIZE = 48; // Pixels

	final public static int SCREEN_CELLS_X = 10;	// Tile multiplier
	final public static int SCREEN_CELLS_Y = 10;	// Tile multiplier

	/* ------------ */
	/* Non-Editable */
	/* ------------ */
	final public static int SCREEN_WIDTH = SCREEN_CELLS_X * CELL_SIZE; 	// Pixels
	final public static int SCREEN_HEIGHT = SCREEN_CELLS_Y * CELL_SIZE;	// Pixels

	final public static double FPS_ALPHA = 1.0 / FPS; // Frame interval multiplier
	final public static double FRAME_INTERVAL_MILLISECONDS = FPS_ALPHA * 1000; // Milliseconds between render frames
}

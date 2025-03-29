/*
 * All configurable game settings
 */

package classes;

public class GameSettings {
	/* -------- */
	/* Editable */
	/* -------- */

	// Text (immutable)
	final public String GAME_HEADER_TITLE = "Bug sim";
	final public String GAME_TITLE = "To be determined";

	// Render settings (mutable)
	public double RENDER_FPS = 1.0 / 60; // Render frames per second
	public int MOVEMENT_FPS = 1; // Movements per second

	// Render settings (immutable)
	final public int CELL_SIZE = 48; // Pixels

	final public int GRID_SIZE_X = 10;	// Cell multiplier
	final public int GRID_SIZE_Y = 10;	// Cell multiplier

	/* ------------ */
	/* Non-Editable */
	/* ------------ */
	final public int SCREEN_WIDTH = GRID_SIZE_X * CELL_SIZE; 	// Pixels
	final public int SCREEN_HEIGHT = GRID_SIZE_Y * CELL_SIZE;	// Pixels

	final public double RENDER_INTERVAL_MILLISECONDS = RENDER_FPS * 1000; // Milliseconds between render frames
	final public double MOVEMENT_INTERVAL_MILLISECONDS = MOVEMENT_FPS * 1000; // Milliseconds between movement frames
}

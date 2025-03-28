/*
 * @Author(s): 
 * @Written: 3/28/2025
 * 
 * class GameScreen:
 * 
 * An extension of the JFrame class, providing only default settings specific for
 * game with few additional methods and functionality. This class is responsible
 * for rendering the top-level system screen window for the game.
 */
package classes;

import javax.swing.JFrame;

public class GameScreen extends JFrame {
	public GameScreen(String title) {
		super();

		// Default game screen settings
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle(title);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	/*
	 * buildUI():
	 * 
	 * A no-arg method that assembles all sub-components of the game
	 * screen.
	 */
	public void buildUI() {
		MasterFrame masterFrame = new MasterFrame();
		this.add(masterFrame);

		// Final UI step: Auto-resize screen to fit contents
		this.pack();
	}
}

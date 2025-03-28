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
	private MasterFrame masterFrame;

	public GameScreen(String title) {
		// Default game screen settings
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setTitle(title);

		// Build UI components
		masterFrame = new MasterFrame();
		this.add(masterFrame);

		// Finalize game screen
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public MasterFrame getMasterFrame() {
		return masterFrame;
	}

	private void drawGameBoard() {

	}
}

/*
 * @Written: 3/28/2025
 * 
 * class GameScreen:
 * 
 * An extension of the JFrame class, providing only default settings specific for
 * game with few additional methods and functionality. This class is responsible
 * for rendering the top-level system screen window for the game.
 */
package src.classes.entity;

import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameScreen extends JFrame {
	final private JPanel masterFrame;

	public GameScreen(Game game) {
		// Default game screen settings
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		this.setTitle(game.getSettings().getTitle());

		// Build UI components
		masterFrame = new JPanel();
		masterFrame.setDoubleBuffered(true);
		masterFrame.setPreferredSize(
			new Dimension(
				game.getSettings().getScreenWidth(),
				game.getSettings().getScreenHeight()
			)
		);
		this.add(masterFrame);

		// Finalize game screen
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		// Handle game window closing event
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				game.terminate();
			}
		});
	}

	public JPanel getMasterFrame() {
		return masterFrame;
	}
}

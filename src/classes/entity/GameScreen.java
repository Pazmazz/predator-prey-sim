/*
 * @written 3/28/2025
 */
package classes.entity;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import classes.util.Console;

/**
 * An extension of the JFrame class, providing only default settings specific
 * for game with few additional methods and functionality. This class is
 * responsible for rendering the top-level system screen window for the game.
 */
public class GameScreen extends JFrame {

	private Game game = Game.getInstance();

	final private JPanel masterFrame;

	public GameScreen() {
		// Default game screen settings
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setResizable(true);
		this.setTitle(game.getSettings().getTitle());

		int width = 500;
		int height = width;
		int cellRows = 10;
		int cellCols = 10;

		// Build UI components
		masterFrame = new JPanel();
		masterFrame.setDoubleBuffered(true);
		masterFrame.setPreferredSize(
				new Dimension(
						game.getSettings().getScreenWidth(),
						game.getSettings().getScreenHeight()));
		this.add(masterFrame);

		//
		masterFrame.setLayout(null);
		ArrayList<JPanel> cells = new ArrayList<>();
		for (int i = 0; i < cellRows; i++) {
			JPanel cell = new JPanel();
			cell.setBackground(Color.BLUE);
			// cell.setSize(new Dimension(100, 100));
			cell.setBounds(i * (masterFrame.getWidth() / cellRows), 0, 10, 10);
			// cell.setPreferredSize(new Dimension(width / cellRows, width / cellRows));

			masterFrame.add(cell);
			cells.add(cell);
		}

		masterFrame.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				for (int i = 0; i < cells.size(); i++) {
					JPanel cell = cells.get(i);
					// cell.setSize(masterFrame.getWidth() / cellRows, masterFrame.getWidth() /
					// cellRows);
					// Console.println("$text-blue Window $text-red resized", i *
					// (masterFrame.getWidth() / cellRows));
					cell.setBounds(i * (masterFrame.getWidth() / cellRows), 0, 10, 10);
				}
			}
		});

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
		return this.masterFrame;
	}
}

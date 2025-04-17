/*
 * @written 3/28/2025
 */
package classes.entity;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import classes.abstracts.Properties.Property;
import classes.entity.CellGrid.Cell;
import classes.settings.GameSettings;

/**
 * An extension of the JFrame class, providing only default settings specific
 * for game with few additional methods and functionality. This class is
 * responsible for rendering the top-level system screen window for the game.
 */
@SuppressWarnings("unused")
public class GameScreen {

	final private Game game = Game.getInstance();
	final private GameSettings settings = game.getSettings();
	final private JFrame window;
	final private JPanel masterFrame;

	final private int ROWS = 20;
	final private int COLS = 20;
	final private int GRID_LINE_THICKNESS = 2;
	final private int SCREEN_WIDTH = settings.getScreenWidth();
	final private int SCREEN_HEIGHT = settings.getScreenHeight();
	final private int CELL_SIZE = (SCREEN_WIDTH - GRID_LINE_THICKNESS) / ROWS;

	public GameScreen() {
		// Default game screen settings
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(true);
		window.setTitle(settings.getTitle());
		window.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
		window.setLocationRelativeTo(null);

		// Build the UI
		masterFrame = buildMainFrame();
		buildHeaderFrame();

		GameGrid grid = new GameGrid();
		masterFrame.add(grid);

		// Handle game window closing event
		window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				game.terminate();
			}
		});
		window.setVisible(true);
	}

	private JPanel buildMainFrame() {
		JPanel masterFrame = new JPanel();
		masterFrame.setLayout(new FlowLayout());
		// masterFrame.setLayout(new BorderLayout());
		// masterFrame.setLayout(new BoxLayout(masterFrame, BoxLayout.Y_AXIS));
		masterFrame.setDoubleBuffered(true);
		masterFrame.setBackground(Color.BLACK);

		window.getContentPane().add(masterFrame);
		return masterFrame;
	}

	private JPanel buildHeaderFrame() {
		JPanel headerFrame = new JPanel();
		headerFrame.setPreferredSize(new Dimension(SCREEN_WIDTH, 40));
		headerFrame.setLayout(new BorderLayout());
		headerFrame.setOpaque(true);

		JLabel titleLabel = new JLabel();
		titleLabel.setBackground(new Color(25, 25, 25));
		titleLabel.setForeground(new Color(25, 255, 0));
		titleLabel.setFont(new Font("Courier New", Font.BOLD, 30));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setText("Predator Prey Simulation");
		titleLabel.setOpaque(true);

		headerFrame.add(titleLabel, BorderLayout.CENTER);
		masterFrame.add(headerFrame);
		return headerFrame;
	}

	public void draw(JPanel gridGUI) {
		CellGrid grid = game.getGameGrid();

		for (Cell cell : grid.getGrid().values()) {
			if (cell.isEmpty())
				continue;

			int col = cell.getUnit2().getX();
			int row = cell.getUnit2().getY();

			JLabel img = getCellImage(cell.getOccupant().getAvatar());
			img.setLocation((col - 1) * CELL_SIZE, (row - 1) * CELL_SIZE);
			gridGUI.add(img);
		}
	}

	private JLabel getCellImage(String imagePath) {
		try {
			BufferedImage imgBuffer = ImageIO.read(new File(imagePath));
			Image img = new ImageIcon(imgBuffer)
					.getImage()
					.getScaledInstance(
							CELL_SIZE,
							CELL_SIZE,
							Image.SCALE_SMOOTH);

			JLabel cellImg = new JLabel(new ImageIcon(img));
			cellImg.setBackground(Color.YELLOW);
			cellImg.setSize(
					CELL_SIZE - GRID_LINE_THICKNESS,
					CELL_SIZE - GRID_LINE_THICKNESS);

			return cellImg;
		} catch (Exception e) {
			throw new Error(e);
		}
	}

	private class GameGrid extends JPanel {
		public GameGrid() {
			setBackground(Color.BLACK);
			setLayout(new BorderLayout());
			setBorder(new EmptyBorder(
					GRID_LINE_THICKNESS,
					GRID_LINE_THICKNESS,
					GRID_LINE_THICKNESS,
					GRID_LINE_THICKNESS));

			JPanel paddedFrame = new JPanel();
			paddedFrame.setLayout(null);
			paddedFrame.setOpaque(false);

			// paddedFrame.add(getCellImage("src/assets/ant2.jpg"));
			// paddedFrame.add(a);
			// paddedFrame.add(b);
			draw(paddedFrame);
			add(paddedFrame);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			g2.setStroke(new BasicStroke(GRID_LINE_THICKNESS));
			g2.setColor(Color.GREEN);

			int lineThicknessOffset = GRID_LINE_THICKNESS / 2;
			int computedCellSize = CELL_SIZE;

			for (int row = 0; row <= ROWS; row++) {
				g2.drawLine(
						0,
						row * computedCellSize + lineThicknessOffset,
						COLS * computedCellSize,
						row * computedCellSize + lineThicknessOffset);
			}

			for (int col = 0; col <= COLS; col++) {
				g2.drawLine(
						col * computedCellSize + lineThicknessOffset,
						0,
						col * computedCellSize + lineThicknessOffset,
						ROWS * computedCellSize);
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(
					COLS * CELL_SIZE + GRID_LINE_THICKNESS,
					ROWS * CELL_SIZE + GRID_LINE_THICKNESS);
		}
	}
}

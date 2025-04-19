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
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import classes.abstracts.Bug;
import classes.abstracts.Properties.Property;
import classes.entity.CellGrid.Cell;
import classes.settings.GameSettings;

import classes.util.Console;

/**
 * An extension of the JFrame class, providing only default settings specific
 * for game with few additional methods and functionality. This class is
 * responsible for rendering the top-level system screen window for the game.
 */
@SuppressWarnings("unused")
public class GameScreen {

	final private Game game = Game.getInstance();
	final private GameSettings settings = game.getSettings();
	final private CellGrid gameGrid = game.getGameGrid();
	final private JFrame window;
	final private JPanel masterFrame;

	final private int ROWS = settings.getGridSize().getY();
	final private int COLS = settings.getGridSize().getX();
	final private int GRID_LINE_THICKNESS = settings.getGridLineThickness();
	final private int SCREEN_WIDTH = settings.getScreenWidth();
	final private int SCREEN_HEIGHT = settings.getScreenHeight();
	final private int CELL_SIZE = (SCREEN_WIDTH - GRID_LINE_THICKNESS) / ROWS;

	final private GameGrid renderedGrid;

	final private HashMap<String, BufferedImage> loadedImages = new HashMap<>();

	public GameScreen() {
		// Default game screen settings
		this.window = new JFrame();
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setResizable(false);
		this.window.setTitle(this.settings.getTitle());
		this.window.setSize(this.SCREEN_WIDTH, this.SCREEN_HEIGHT);
		this.window.setLocationRelativeTo(null);

		this.loadImages();

		// Build the UI
		this.masterFrame = buildMainFrame();
		this.buildHeaderFrame();

		this.renderedGrid = new GameGrid();
		this.masterFrame.add(this.renderedGrid);

		// Handle game window closing event
		this.window.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				game.terminate();
			}
		});
		this.window.setVisible(true);
	}

	public void loadImages() {
		try {
			this.loadedImages.put("ant", ImageIO.read(new File("src/Assets/ant2.jpg")));
			this.loadedImages.put("doodlebug", ImageIO.read(new File("src/Assets/doodlebug3.jpg")));
			this.loadedImages.put("titan", ImageIO.read(new File("src/Assets/titanant.jpg")));
			this.loadedImages.put("pathcell", ImageIO.read(new File("src/Assets/pathcell.jpg")));
		} catch (Exception e) {
			throw new Error("Error loading images");
		}
	}

	public void repaintGrid() {
		this.renderedGrid.repaint();
	}

	private JPanel buildMainFrame() {
		JPanel masterFrame = new JPanel();
		masterFrame.setLayout(new FlowLayout());
		// masterFrame.setLayout(new BorderLayout());
		// masterFrame.setLayout(new BoxLayout(masterFrame, BoxLayout.Y_AXIS));
		masterFrame.setDoubleBuffered(true);
		masterFrame.setBackground(Color.BLACK);

		this.window.getContentPane().add(masterFrame);
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
		this.masterFrame.add(headerFrame);
		return headerFrame;
	}

	private class GameGrid extends JPanel {
		final private JPanel contentFrame;

		public GameGrid() {
			this.setBackground(Color.BLACK);
			this.setLayout(new BorderLayout());
			this.setBorder(new EmptyBorder(
					GRID_LINE_THICKNESS,
					GRID_LINE_THICKNESS,
					GRID_LINE_THICKNESS,
					GRID_LINE_THICKNESS));

			this.contentFrame = new JPanel();
			this.contentFrame.setLayout(null);
			this.contentFrame.setOpaque(false);

			this.add(contentFrame);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			g2.setStroke(new BasicStroke(GRID_LINE_THICKNESS));
			g2.setColor(Color.GREEN);

			int lineThicknessOffset = GRID_LINE_THICKNESS / 2;

			for (int row = 0; row <= ROWS; row++) {
				g2.drawLine(
						GRID_LINE_THICKNESS,
						row * CELL_SIZE + lineThicknessOffset,
						COLS * CELL_SIZE,
						row * CELL_SIZE + lineThicknessOffset);
			}

			for (int col = 0; col <= COLS; col++) {
				g2.drawLine(
						col * CELL_SIZE + lineThicknessOffset,
						GRID_LINE_THICKNESS,
						col * CELL_SIZE + lineThicknessOffset,
						ROWS * CELL_SIZE);
			}

			for (Cell cell : gameGrid.getGrid().values()) {
				int row = cell.getUnit2().getY();
				int col = cell.getUnit2().getX();

				if (cell.isEmpty())
					continue;

				Bug<?> occupant = (Bug<?>) cell.getOccupant();

				if (cell.getOccupant() instanceof Ant) {
					g2.drawImage(
							loadedImages.get("ant"),
							GRID_LINE_THICKNESS + (col - 1) * CELL_SIZE,
							GRID_LINE_THICKNESS + (row - 1) * CELL_SIZE,
							CELL_SIZE - GRID_LINE_THICKNESS,
							CELL_SIZE - GRID_LINE_THICKNESS,
							contentFrame);
				} else if (cell.getOccupant() instanceof Doodlebug) {
					g2.drawImage(
							loadedImages.get("doodlebug"),
							GRID_LINE_THICKNESS + (col - 1) * CELL_SIZE,
							GRID_LINE_THICKNESS + (row - 1) * CELL_SIZE,
							CELL_SIZE - GRID_LINE_THICKNESS,
							CELL_SIZE - GRID_LINE_THICKNESS,
							contentFrame);
				} else if (cell.getOccupant() instanceof Titan) {
					g2.drawImage(
							loadedImages.get("titan"),
							GRID_LINE_THICKNESS + (col - 1) * CELL_SIZE,
							GRID_LINE_THICKNESS + (row - 1) * CELL_SIZE,
							CELL_SIZE - GRID_LINE_THICKNESS,
							CELL_SIZE - GRID_LINE_THICKNESS,
							contentFrame);
				}

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

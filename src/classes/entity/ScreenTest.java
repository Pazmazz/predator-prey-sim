package classes.entity;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import classes.entity.CellGrid.Cell;
import classes.settings.GameSettings;
import classes.util.Console;
import classes.util.Math2;

@SuppressWarnings("unused")
public class ScreenTest {
	final private static Game game = Game.getInstance();
	final private static GameSettings settings = game.getSettings();
	final private static CellGrid gameGrid = game.getGameGrid();

	final private JFrame window;
	final private JPanel masterFrame;

	final private int GRID_LINE_THICKNESS = settings.getGridLineThickness();
	final private int GRID_BORDER_PADDING = settings.getGridBorderPadding();
	final private int GRID_LINE_OFFSET = (int) Math.ceil(GRID_LINE_THICKNESS / 2.0);

	final private int ROWS = settings.getGridSize().getY();
	final private int COLS = settings.getGridSize().getX();
	final private int SCREEN_WIDTH = settings.getScreenWidth();
	final private int CELL_SIZE = (SCREEN_WIDTH - GRID_BORDER_PADDING * 2) / COLS;

	public ScreenTest() {
		// Default game screen settings
		this.window = new JFrame();
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setResizable(true);
		this.window.setTitle("Test");

		this.masterFrame = buildMainFrame();
		this.masterFrame.add(new GridPanel());

		// JButton butt = new JButton();
		// butt.setPreferredSize(new Dimension(100, 50));
		// butt.addActionListener(e -> {
		// Console.println("Worked");
		// });
		// this.masterFrame.add(butt);

		Console.println("Cell size: ", CELL_SIZE);

		this.window.pack();
		this.window.setLocationRelativeTo(null);
		this.window.setVisible(true);
	}

	private JPanel buildMainFrame() {
		JPanel masterFrame = new JPanel();
		masterFrame.setLayout(new BoxLayout(masterFrame, BoxLayout.Y_AXIS));
		// masterFrame.setBorder(new EmptyBorder(100, 100, 100, 100));
		masterFrame.setDoubleBuffered(true);
		masterFrame.setBackground(Color.BLACK);

		this.window.getContentPane().add(masterFrame);
		return masterFrame;
	}

	private JPanel square() {
		JPanel square = new JPanel();
		square.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
		square.setSize(new Dimension(20, 20));
		// square.setLocation(20, 20);
		square.setBackground(Color.RED);
		return square;
	}

	private class GridPanel extends JPanel {

		final private int halfGridLineThickness = GRID_LINE_THICKNESS / 2;

		public GridPanel() {
			this.setBackground(Color.BLUE);
			this.setLayout(null);
			this.add(square());
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			g2.setStroke(new BasicStroke(GRID_LINE_THICKNESS));

			int gridStart = GRID_BORDER_PADDING;
			int gridEnd = SCREEN_WIDTH - GRID_BORDER_PADDING;

			g2.setColor(Color.GRAY);
			for (int row = 0; row <= ROWS; row++) {
				int Y = gridStart + row * CELL_SIZE;
				g2.drawLine(gridStart, Y, gridEnd, Y);
			}

			g2.setColor(Color.DARK_GRAY);
			for (int col = 0; col <= COLS; col++) {
				int X = gridStart + col * CELL_SIZE;
				g2.drawLine(X, gridStart, X, gridEnd);
			}

			Unit2[] points = new Unit2[] {
					new Unit2(2, 2),
					new Unit2(5, 3),
					new Unit2(6, 5),
					new Unit2(13, 13)
			};

			g2.setColor(Color.YELLOW);
			for (Unit2 unit : points) {
				int row = unit.getY();
				int col = unit.getX();

				int posX = gridStart + (col - 1) * CELL_SIZE + GRID_LINE_OFFSET;
				int posY = gridStart + (row - 1) * CELL_SIZE + GRID_LINE_OFFSET;

				g2.fillRect(
						posX,
						posY,
						CELL_SIZE - GRID_LINE_THICKNESS,
						CELL_SIZE - GRID_LINE_THICKNESS);
			}

		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(
					SCREEN_WIDTH,
					SCREEN_WIDTH);
		}
	}
}

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
	final private JPanel gridContent;

	final private int GRID_LINE_THICKNESS = settings.getGridLineThickness();
	final private int GRID_BORDER_PADDING = settings.getGridBorderPadding();
	final private double GRID_LINE_OFFSET = GRID_LINE_THICKNESS / 2.0;

	final private int ROWS = settings.getGridSize().getY();
	final private int COLS = settings.getGridSize().getX();
	final private int SCREEN_WIDTH = settings.getScreenWidth();
	final private int CONTENT_WIDTH = (SCREEN_WIDTH - GRID_BORDER_PADDING * 2);
	final private double CELL_SIZE = CONTENT_WIDTH / (double) COLS;

	final private int GRID_START = GRID_BORDER_PADDING;
	final private int GRID_END = SCREEN_WIDTH - GRID_BORDER_PADDING;

	public ScreenTest() {
		// Default game screen settings
		this.window = new JFrame();
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setResizable(true);
		this.window.setTitle("Test");

		this.masterFrame = buildMasterFrame();

		JPanel grid = new GridPanel();
		this.masterFrame.add(grid);

		this.gridContent = new GridContent(grid);
		grid.add(this.gridContent);

		this.window.pack();
		this.window.setLocationRelativeTo(null);
		this.window.setVisible(true);
	}

	private int toPixel(double n) {
		return (int) Math.ceil(n);
	}

	private Unit2 cellUnitToScreenPosition(Unit2 cellUnit) {
		int row = cellUnit.getY();
		int col = cellUnit.getX();

		int posX = toPixel(this.GRID_START + (col - 1) * this.CELL_SIZE + this.GRID_LINE_OFFSET);
		int posY = toPixel(this.GRID_START + (row - 1) * this.CELL_SIZE + this.GRID_LINE_OFFSET);

		return new Unit2(posX, posY);
	}

	private Unit2 getCellSize() {
		return new Unit2(
				toPixel(CELL_SIZE - GRID_LINE_THICKNESS),
				toPixel(CELL_SIZE - GRID_LINE_THICKNESS));
	}

	private JPanel buildMasterFrame() {
		JPanel masterFrame = new JPanel();
		masterFrame.setLayout(new BoxLayout(masterFrame, BoxLayout.Y_AXIS));
		masterFrame.setBorder(new EmptyBorder(10, 10, 10, 10));
		masterFrame.setDoubleBuffered(true);
		masterFrame.setBackground(Color.BLACK);

		this.window.getContentPane().add(masterFrame);
		return masterFrame;
	}

	private class GridContent extends JPanel {
		final private JPanel parentGrid;
		final private int parentWidth;
		final private int parentHeight;

		public GridContent(JPanel grid) {
			this.parentGrid = grid;
			this.parentWidth = toPixel(grid.getPreferredSize().getWidth());
			this.parentHeight = toPixel(grid.getPreferredSize().getHeight());
			this.setLayout(null);
			this.setOpaque(false);
			this.setSize(this.parentWidth, this.parentHeight);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			Unit2[] points = new Unit2[] {
					new Unit2(2, 2),
					new Unit2(5, 3),
					new Unit2(6, 5),
					new Unit2(13, 13)
			};

			g2.setColor(Color.YELLOW);
			for (Unit2 unit : points) {
				Unit2 cellPos = cellUnitToScreenPosition(unit);
				Unit2 cellSize = getCellSize();
				g2.fillRect(
						cellPos.getX(), cellPos.getY(),
						cellSize.getX(), cellSize.getY());
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(
					this.parentWidth,
					this.parentHeight);
		}
	}

	private class GridPanel extends JPanel {

		public GridPanel() {
			this.setBackground(Color.BLUE);
			this.setLayout(null);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			g2.setStroke(new BasicStroke(GRID_LINE_THICKNESS));

			g2.setColor(Color.CYAN);
			for (int row = 0; row <= ROWS; row++) {
				int Y = toPixel(GRID_START + row * CELL_SIZE);
				g2.drawLine(GRID_START, Y, GRID_END, Y);
			}

			g2.setColor(Color.CYAN);
			for (int col = 0; col <= COLS; col++) {
				int X = toPixel(GRID_START + col * CELL_SIZE);
				g2.drawLine(X, GRID_START, X, GRID_END);
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

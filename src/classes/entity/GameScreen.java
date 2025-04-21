package classes.entity;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.border.EmptyBorder;

import classes.abstracts.Bug;
import classes.abstracts.Entity;
import classes.abstracts.Entity.EntityVariant;
import classes.abstracts.Properties.Property;
import classes.entity.CellGrid.Cell;
import classes.entity.Game.SimulationState;
import classes.settings.GameSettings;
import classes.simulation.MovementFrame;
import classes.util.Console;
import classes.util.Math2;
import classes.util.Time;

@SuppressWarnings("unused")
public class GameScreen {
	final private static Game game = Game.getInstance();
	final private static GameSettings settings = game.getSettings();
	final private static CellGrid gameGrid = game.getGameGrid();

	final private JFrame window;
	final private JPanel victoryScreen;
	final private JComponent windowOverlay;
	final private JPanel contentFrame;
	final private JPanel gridContent;
	final private JPanel grid;
	final private JLabel tooltip;

	final private int GRID_LINE_THICKNESS = settings.getGridLineThickness();
	final private int GRID_BORDER_PADDING = settings.getGridBorderPadding();
	final private double GRID_LINE_OFFSET = GRID_LINE_THICKNESS / 2.0;

	final private int ROWS = settings.getGridSize().getY();
	final private int COLS = settings.getGridSize().getX();
	final private int SCREEN_WIDTH = settings.getScreenWidth();
	final private int GRID_CONTENT_WIDTH = (SCREEN_WIDTH - GRID_BORDER_PADDING * 2);
	final private double CELL_SIZE = GRID_CONTENT_WIDTH / (double) COLS;

	final private int GRID_START = GRID_BORDER_PADDING;
	final private int GRID_END = SCREEN_WIDTH - GRID_BORDER_PADDING;

	final private HashMap<IMAGE, BufferedImage> loadedImages = new HashMap<>();

	public enum IMAGE {
		BASE_ANT,
		BASE_DOODLEBUG,
		BASE_TITAN,
		RED_CELL,
		ANT_PROFILE,
	}

	public GameScreen() {
		// Default game screen settings
		JFrame window = new JFrame();
		this.window = window;
		this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.window.setResizable(false);
		this.window.setTitle("Test");

		this.loadImages();

		// window overlays
		JComponent windowOverlay = (JComponent) window.getGlassPane();
		windowOverlay.setLayout(null);
		windowOverlay.setVisible(true);
		windowOverlay.setOpaque(false);
		this.windowOverlay = windowOverlay;

		JLabel tooltip = buildTooltip();
		this.tooltip = tooltip;
		windowOverlay.add(tooltip);

		// main components
		JPanel contentFrame = buildContentFrame();
		this.contentFrame = contentFrame;

		JPanel headerPanel = this.buildHeaderPanel();
		contentFrame.add(headerPanel);

		GridPanel grid = new GridPanel();
		this.grid = grid;
		contentFrame.add(grid);

		GridContent gridContent = new GridContent(grid);
		this.gridContent = gridContent;
		grid.add(gridContent);

		VictoryScreen victoryScreen = new VictoryScreen();
		this.victoryScreen = victoryScreen;
		// windowOverlay.add(victoryScreen);
		grid.getOverlay().add(victoryScreen);

		this.window.pack();
		this.window.setLocationRelativeTo(null);
		this.window.setVisible(true);

		game.onSimulationStateChanged.connect(data -> {
			SimulationState state = (SimulationState) data[0];
			if (state != SimulationState.ENDED) {
				victoryScreen.conceal();
			} else {
				tooltip.setVisible(false);
				victoryScreen.update();
				victoryScreen.display();
			}
		});
	}

	public void loadImages() {
		try {
			this.loadedImages.put(IMAGE.BASE_ANT, ImageIO.read(new File("src/assets/ant2.jpg")));
			this.loadedImages.put(IMAGE.BASE_DOODLEBUG, ImageIO.read(new File("src/assets/doodlebug3.jpg")));
			this.loadedImages.put(IMAGE.BASE_TITAN, ImageIO.read(new File("src/assets/titanant.jpg")));
			this.loadedImages.put(IMAGE.RED_CELL, ImageIO.read(new File("src/assets/pathcell.jpg")));
			this.loadedImages.put(IMAGE.ANT_PROFILE, ImageIO.read(new File("src/assets/ant3.jpg")));
		} catch (Exception e) {
			throw new Error("Error loading images");
		}
	}

	public void renderGrid() {
		this.gridContent.repaint();
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

	private Cell getCellFromScreenPoint(Point point) {
		double relX = point.getX() / SCREEN_WIDTH * COLS;
		double relY = point.getY() / SCREEN_WIDTH * ROWS;
		Vector2 gridPoint = new Vector2(relX, relY);
		return game.getGameGrid().getCell(gridPoint);
	}

	private void renderCell(JPanel grid, Graphics2D g2, Cell cell) {
		if (cell.isEmpty())
			return;

		Entity<?> entity = cell.getOccupant();

		// TODO: temp solution to fixing null entity, fix this later
		if (entity == null) {
			return;
		}

		Unit2 cellPos = cellUnitToScreenPosition(cell.getUnit2());
		Unit2 cellSize = getCellSize();

		int posX = cellPos.getX();
		int posY = cellPos.getY();
		int sizeX = cellSize.getX();
		int sizeY = cellSize.getY();

		// RENDER CELL
		if (entity instanceof Doodlebug) {
			Doodlebug db = (Doodlebug) entity;
			double hungerAlpha = 1 - db.getHungerMeter().getRatio();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) hungerAlpha));
		} else if (entity instanceof Bug<?>) {
			Bug<?> bug = (Bug<?>) entity;
			double timeAliveAlpha = Math.min(1, Time.nanoToSeconds(bug.getTimeSinceBirth()) / 0.3);
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (float) timeAliveAlpha));
		}

		// TODO: Very rare error case where "entity" is null here, fix later
		g2.drawImage(
				loadedImages.get(entity.getAvatar()),
				posX,
				posY,
				sizeX,
				sizeY,
				grid);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}

	private class VictoryScreen extends JPanel {
		private JLabel winnerTitle;
		private JLabel winnerIcon;

		public VictoryScreen() {
			this.setBackground(new Color(0, 0, 0, 150));
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setBorder(new EmptyBorder(20, 10, 10, 20));
			this.setBounds(0, 0, SCREEN_WIDTH, SCREEN_WIDTH);
			this.setVisible(false);

			JPanel headerContent = new JPanel();
			headerContent.setOpaque(true);

			JLabel winnerHeader = new JLabel();
			winnerHeader.setFont(new Font("Showcard Gothic", Font.BOLD, 25));
			winnerHeader.setText("<html><div style='color:#FF5BB2;'>W I N N E R</div></html>");
			headerContent.add(winnerHeader);

			JLabel winnerIcon = new JLabel();
			winnerIcon.setIcon(new ImageIcon(loadedImages.get(IMAGE.BASE_DOODLEBUG)));
			winnerIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.winnerIcon = winnerIcon;

			JPanel centerContent = new JPanel();
			centerContent.setLayout(new BorderLayout());
			centerContent.setOpaque(true);

			JLabel winnerTitle = new JLabel();
			winnerTitle.setForeground(Color.GREEN);
			winnerTitle.setFont(new Font("Courier New", Font.BOLD, 20));
			winnerTitle.setText("Default");
			winnerTitle.setHorizontalAlignment(SwingConstants.CENTER);
			this.winnerTitle = winnerTitle;

			centerContent.add(winnerTitle, BorderLayout.NORTH);

			// JLabel title = new JLabel();
			// // title.setBorder(new EmptyBorder(10, 10, 10, 10));
			// title.setText("Predator Prey Simulation");
			// title.setFont(new Font("Courier New", Font.BOLD, 30));
			// title.setAlignmentX(JFrame.CENTER_ALIGNMENT);
			// title.setForeground(new Color(25, 255, 0));

			// this.add(winnerHeader);
			this.add(headerContent);
			this.add(Box.createVerticalStrut(15));
			this.add(winnerIcon);
			this.add(Box.createVerticalStrut(15));
			this.add(centerContent);
		}

		public void display() {
			this.setVisible(true);
		}

		public void conceal() {
			this.setVisible(false);
		}

		public void update() {
			MovementFrame movementFrame = game.getMovementFrame();
			EntityVariant winner = movementFrame.getWinner();
			Doodlebug dbMVP = movementFrame.getCurrentDoodlebugMVP();
			Ant antMVP = movementFrame.getCurrentAntMPV();

			Bug<?> bugMVP = null;
			ImageIcon mvpIcon = null;

			switch (winner) {
				case DOODLEBUG -> {
					bugMVP = dbMVP;
					mvpIcon = new ImageIcon(loadedImages.get(IMAGE.BASE_DOODLEBUG));
				}
				case ANT -> {
					bugMVP = antMVP;
					mvpIcon = new ImageIcon(loadedImages.get(IMAGE.ANT_PROFILE));
				}
				case TITAN -> bugMVP = null;
				default -> {
					bugMVP = antMVP;
				}
			}

			this.winnerTitle.setText("<html>" + bugMVP.getName() + "</html>");
			this.winnerIcon.setIcon(mvpIcon);
		}
	}

	private JLabel buildTooltip() {
		JLabel tooltip = new JLabel();
		// tooltip.setBounds(0, 0, 100, 20);
		tooltip.setText("Some");
		tooltip.setBackground(Color.BLACK);
		tooltip.setFont(new Font("Courier New", Font.BOLD, 16));
		tooltip.setVisible(true);
		tooltip.setForeground(new Color(25, 255, 0));
		tooltip.setOpaque(true);

		return tooltip;
	}

	private JPanel buildContentFrame() {
		JPanel contentFrame = new JPanel();
		contentFrame.setLayout(new BoxLayout(contentFrame, BoxLayout.Y_AXIS));
		// contentFrame.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentFrame.setDoubleBuffered(true);
		contentFrame.setBackground(Color.BLACK);

		this.window.getContentPane().add(contentFrame);
		return contentFrame;
	}

	private JPanel buildHeaderPanel() {
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
		// headerPanel.setBackground(Color.RED);
		headerPanel.setOpaque(false);
		headerPanel.setPreferredSize(new Dimension(0, 60));

		JLabel title = new JLabel();
		// title.setBorder(new EmptyBorder(10, 10, 10, 10));
		title.setText("Predator Prey Simulation");
		title.setFont(new Font("Courier New", Font.BOLD, 30));
		title.setAlignmentX(JFrame.CENTER_ALIGNMENT);
		title.setForeground(new Color(25, 255, 0));

		JPanel mainToolbar = new JPanel();
		mainToolbar.setLayout(new GridLayout());
		mainToolbar.setBackground(Color.GRAY);

		JButton initGridButton = new JButton();
		initGridButton.setText("INIT");
		// startButton.setFont(new Font("MV Boli", Font.BOLD, 20));
		initGridButton.setBackground(new Color(25, 25, 25));
		initGridButton.setForeground(Color.WHITE);
		initGridButton.setFocusable(false);

		initGridButton.addActionListener(e -> {
			if (game.getSimulationState() != SimulationState.INITIAL)
				return;

			Console.benchmark("Initializing game grid", game::initGameGrid);
			game.setSimulationState(SimulationState.STARTED);
		});

		JButton toggleSimulationButton = new JButton();
		toggleSimulationButton.setText("▶");
		// startButton.setFont(new Font("MV Boli", Font.BOLD, 20));
		toggleSimulationButton.setBackground(Color.GREEN);
		toggleSimulationButton.setForeground(Color.BLACK);
		toggleSimulationButton.setFocusable(false);

		game.onSimulationStateChanged.connect(data -> {
			MovementFrame movementFrame = game.getMovementFrame();
			SimulationState state = (SimulationState) data[0];
			if (state == SimulationState.PAUSED || state == SimulationState.INITIAL
					|| state == SimulationState.ENDED
					|| state == SimulationState.MANUAL) {
				toggleSimulationButton.setText("▶");
				toggleSimulationButton.setBackground(Color.GREEN);
				movementFrame.suspend();
			} else if (state == SimulationState.RUNNING) {
				toggleSimulationButton.setText("⏸︎");
				toggleSimulationButton.setBackground(Color.ORANGE);
				movementFrame.resume();
			}
		});

		toggleSimulationButton.addActionListener(e -> {
			SimulationState simState = game.getSimulationState();

			if (simState == SimulationState.PAUSED
					|| simState == SimulationState.STARTED || simState == SimulationState.MANUAL
					|| simState == SimulationState.INITIAL) {
				if (!game.onCurrentSnapshot()) {
					game.loadMostRecentSnapshot();
				}
				game.setSimulationState(SimulationState.RUNNING);
			} else if (game.getSimulationState() == SimulationState.RUNNING) {
				game.setSimulationState(SimulationState.PAUSED);
			} else {
				Console.println("$text-yellow No play state available");
			}
		});

		JButton stepForwardButton = new JButton();
		stepForwardButton.setText(">");
		// startButton.setFont(new Font("MV Boli", Font.BOLD, 20));
		stepForwardButton.setBackground(new Color(25, 25, 25));
		stepForwardButton.setForeground(Color.WHITE);
		stepForwardButton.setFocusable(false);

		stepForwardButton.addActionListener(e -> {
			if (game.getSimulationState() == SimulationState.ENDED)
				return;

			game.setSimulationState(SimulationState.MANUAL);
			game.loadNextSnapshot();
		});

		JButton stepBackButton = new JButton();
		stepBackButton.setText("<");
		// startButton.setFont(new Font("MV Boli", Font.BOLD, 20));
		stepBackButton.setBackground(new Color(25, 25, 25));
		stepBackButton.setForeground(Color.WHITE);
		stepBackButton.setFocusable(false);

		stepBackButton.addActionListener(e -> {
			game.setSimulationState(SimulationState.MANUAL);
			game.loadPrevSnapshot();
		});

		JButton clearButton = new JButton();
		clearButton.setText("CLEAR");
		// startButton.setFont(new Font("MV Boli", Font.BOLD, 20));
		clearButton.setBackground(new Color(25, 25, 25));
		clearButton.setForeground(Color.WHITE);
		clearButton.setFocusable(false);

		clearButton.addActionListener(e -> {
			game.getGameGrid().clearCells();
			game.setSimulationState(SimulationState.INITIAL);
		});

		JButton downloadButton = new JButton();
		downloadButton.setText("DOWNLOAD");
		// startButton.setFont(new Font("MV Boli", Font.BOLD, 20));
		downloadButton.setBackground(new Color(25, 25, 25));
		downloadButton.setForeground(Color.WHITE);
		downloadButton.setFocusable(false);

		downloadButton.addActionListener(e -> {
			System.out.println(game.getGameGrid().download());
		});

		JButton fasterButton = new JButton();
		fasterButton.setText("+");
		// startButton.setFont(new Font("MV Boli", Font.BOLD, 20));
		fasterButton.setBackground(new Color(25, 25, 25));
		fasterButton.setForeground(Color.WHITE);
		fasterButton.setFocusable(false);

		fasterButton.addActionListener(e -> {
			settings.setAntMovementCooldown(Math.max(0.001, settings.getAntMovementCooldown() / 1.2));
			settings.setDoodlebugMovementCooldown(Math.max(0.001, settings.getDoodlebugMovementCooldown() / 1.2));
			for (Cell cell : gameGrid.getGrid().values()) {
				if (cell.hasOccupant()) {
					if (cell.getOccupant() instanceof Ant) {
						cell.getOccupant().setProperty(Property.MOVEMENT_COOLDOWN,
								settings.getAntMovementCooldown());
					} else if (cell.getOccupant() instanceof Doodlebug) {
						cell.getOccupant().setProperty(Property.MOVEMENT_COOLDOWN,
								settings.getDoodlebugMovementCooldown());
					}
				}
			}
		});

		JButton slowerButton = new JButton();
		slowerButton.setText("-");
		// startButton.setFont(new Font("MV Boli", Font.BOLD, 20));
		slowerButton.setBackground(new Color(25, 25, 25));
		slowerButton.setForeground(Color.WHITE);
		slowerButton.setFocusable(false);

		slowerButton.addActionListener(e -> {
			settings.setAntMovementCooldown(Math.min(1.0, settings.getAntMovementCooldown() * 1.2));
			settings.setDoodlebugMovementCooldown(Math.min(1.0, settings.getDoodlebugMovementCooldown() * 1.2));
			for (Cell cell : gameGrid.getGrid().values()) {
				if (cell.hasOccupant()) {
					if (cell.getOccupant() instanceof Ant) {
						cell.getOccupant().setProperty(Property.MOVEMENT_COOLDOWN,
								settings.getAntMovementCooldown());
					} else if (cell.getOccupant() instanceof Doodlebug) {
						cell.getOccupant().setProperty(Property.MOVEMENT_COOLDOWN,
								settings.getDoodlebugMovementCooldown());
					}
				}
			}
		});

		mainToolbar.add(slowerButton);
		mainToolbar.add(fasterButton);
		mainToolbar.add(downloadButton);
		mainToolbar.add(clearButton);
		mainToolbar.add(stepBackButton);
		mainToolbar.add(stepForwardButton);
		mainToolbar.add(toggleSimulationButton);
		mainToolbar.add(initGridButton);

		//
		headerPanel.add(title);
		headerPanel.add(mainToolbar);
		return headerPanel;
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
			this.setSize(this.parentWidth, this.parentHeight);
			// this.setSize(300, 300);
			this.setOpaque(false);

		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			for (Cell cell : gameGrid.getGrid().values()) {
				renderCell(this, g2, cell);
			}

			g2.dispose();
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(
					this.parentWidth,
					this.parentHeight);
		}
	}

	private class GridPanel extends JPanel {

		private JPanel overlay;

		public GridPanel() {
			JPanel overlay = new JPanel();
			overlay.setLayout(null);
			overlay.setSize(SCREEN_WIDTH, SCREEN_WIDTH);
			overlay.setOpaque(false);
			this.overlay = overlay;
			this.add(overlay);

			this.setBackground(settings.getGridBackgroundColor());
			this.setLayout(null);
			// this.setBorder(new EmptyBorder(0, 0, 0, 0));
			JPanel gridPanel = this;

			this.addMouseMotionListener(new MouseMotionAdapter() {
				@Override
				public void mouseMoved(MouseEvent e) {
					if (game.getSimulationState() == SimulationState.ENDED)
						return;

					Point point = e.getPoint();
					Point gridLocation = gridPanel.getLocation();
					Cell hoveredCell = getCellFromScreenPoint(point);

					if (hoveredCell.hasOccupant()) {
						Entity<?> entity = hoveredCell.getOccupant();
						if (entity instanceof Bug<?>) {
							Bug<?> bug = (Bug<?>) entity;
							tooltip.setText("<html>" + bug.getTooltipString() + "</html>");
							Dimension tooltipSize = tooltip.getPreferredSize();

							int tooltipX = (int) (gridLocation.getX() + point.getX());
							int tooltipY = (int) (gridLocation.getY() + point.getY() -
									tooltipSize.getHeight());

							if (tooltipX + tooltipSize.getWidth() > SCREEN_WIDTH) {
								tooltipX -= tooltipSize.getWidth();
							}

							tooltip.setLocation(tooltipX, tooltipY);
							tooltip.setVisible(true);
							tooltip.setSize(tooltipSize);
						}
					} else {
						tooltip.setVisible(false);
					}
				}
			});

			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseExited(MouseEvent e) {
					tooltip.setVisible(false);
				}
			});
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			g2.setStroke(new BasicStroke(GRID_LINE_THICKNESS));

			g2.setColor(settings.getGridLinesColor());
			for (int row = 0; row <= ROWS; row++) {
				int Y = toPixel(GRID_START + row * CELL_SIZE);
				g2.drawLine(GRID_START, Y, GRID_END, Y);
			}

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

		public JPanel getOverlay() {
			return this.overlay;
		}
	}
}

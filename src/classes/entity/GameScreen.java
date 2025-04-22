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

	final private HashMap<ImageSet, BufferedImage> loadedImages = new HashMap<>();

	public enum ImageSet {
		BASE_ANT,
		BASE_DOODLEBUG,
		BASE_TITAN,
		RED_CELL,
		ANT_PROFILE,
	}

	public enum InteractivityState {

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
			this.loadedImages.put(ImageSet.BASE_ANT, ImageIO.read(new File("src/assets/ant2.jpg")));
			this.loadedImages.put(ImageSet.BASE_DOODLEBUG, ImageIO.read(new File("src/assets/doodlebug3.jpg")));
			this.loadedImages.put(ImageSet.BASE_TITAN, ImageIO.read(new File("src/assets/titanant.jpg")));
			this.loadedImages.put(ImageSet.RED_CELL, ImageIO.read(new File("src/assets/pathcell.jpg")));
			this.loadedImages.put(ImageSet.ANT_PROFILE, ImageIO.read(new File("src/assets/ant3.jpg")));
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

		if (entity.hasTag()) {
			g2.setColor(Color.YELLOW);
			g2.fillRect(posX, posY, 20, 20);
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
		private JLabel timeInSimLabel;
		private JLabel antsEatenLabel;
		private JLabel generationLabel;

		private JLabel secondaryWinnerTitle;
		private JLabel secondaryWinnerIcon;
		private JLabel secondaryTimeInSimLabel;
		private JLabel secondaryAntsEatenLabel;
		private JLabel secondaryGenerationLabel;

		public VictoryScreen() {
			/*
			 * IMPORTANT:
			 * 
			 * BorderLayout will always PUSH FlowLayout (the default layout of components).
			 * This can be used to auto-size FlowLayout components by placing a BorderLayout
			 * component above or beneath them.
			 */
			this.setBackground(new Color(0, 0, 0, 180));
			this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			this.setBorder(new EmptyBorder(20, 10, 10, 20));
			this.setBounds(0, 0, SCREEN_WIDTH, SCREEN_WIDTH);
			this.setVisible(false);

			boolean showBackgrounds = false;

			// HEADER CONTENT
			JPanel headerContent = new JPanel();
			headerContent.setOpaque(showBackgrounds);

			JLabel winnerHeader = new JLabel();
			winnerHeader.setFont(new Font("Showcard Gothic", Font.BOLD, 20));
			winnerHeader.setText("<html><div style='color:#FF5BB2;'>W I N N E R</div></html>");
			headerContent.add(winnerHeader);

			// WINNER ICON
			JLabel winnerIcon = new JLabel();
			winnerIcon.setIcon(new ImageIcon(loadedImages.get(ImageSet.BASE_DOODLEBUG)));
			winnerIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.winnerIcon = winnerIcon;

			// WINNER TITLE CONTENT
			JPanel centerContent = new JPanel();
			centerContent.setLayout(new FlowLayout(FlowLayout.CENTER));
			centerContent.setOpaque(showBackgrounds);

			JLabel mvpLabel = new JLabel();
			mvpLabel.setForeground(Color.YELLOW);
			mvpLabel.setFont(new Font("Felix Titling", Font.BOLD, 10));
			mvpLabel.setText("MVP");
			mvpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			mvpLabel.setOpaque(showBackgrounds);

			JLabel winnerTitle = new JLabel();
			winnerTitle.setForeground(Color.GREEN);
			winnerTitle.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 15));
			winnerTitle.setText("Default");
			winnerTitle.setHorizontalAlignment(SwingConstants.CENTER);
			this.winnerTitle = winnerTitle;

			centerContent.add(winnerTitle);

			// BODY CONTENT
			JPanel mainContent = new JPanel();
			mainContent.setLayout(new BorderLayout());
			mainContent.setBackground(Color.RED);
			mainContent.setOpaque(showBackgrounds);

			// JPanel innerMainContent = new JPanel();
			// // innerMainContent.setLayout(new GridLayout(3, 0, 20, 20));
			// innerMainContent.setOpaque(true);
			// innerMainContent.setBorder(new EmptyBorder(0, 20, 0, 20));
			// mainContent.add(innerMainContent, BorderLayout.NORTH);

			JPanel timeInSimFrame = new JPanel();
			timeInSimFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
			timeInSimFrame.setOpaque(showBackgrounds);

			JLabel timeInSimLabel = new JLabel();
			timeInSimLabel.setForeground(Color.WHITE);
			timeInSimLabel.setFont(new Font("Courier New", Font.PLAIN, 15));
			timeInSimLabel.setText("Time in Simulation: ");
			this.timeInSimLabel = timeInSimLabel;
			timeInSimFrame.add(timeInSimLabel);
			// innerMainContent.add(timeInSimLabel);

			JPanel antsEatenFrame = new JPanel();
			antsEatenFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
			antsEatenFrame.setOpaque(showBackgrounds);

			JLabel antsEatenLabel = new JLabel();
			antsEatenLabel.setForeground(Color.WHITE);
			antsEatenLabel.setFont(new Font("Courier New", Font.PLAIN, 15));
			antsEatenLabel.setText("Ants Eaten: ");
			this.antsEatenLabel = antsEatenLabel;
			antsEatenFrame.add(antsEatenLabel);
			// innerMainContent.add(antsEatenLabel);

			JPanel generationFrame = new JPanel();
			generationFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
			generationFrame.setOpaque(showBackgrounds);

			JLabel generationLabel = new JLabel();
			generationLabel.setForeground(Color.WHITE);
			generationLabel.setFont(new Font("Courier New", Font.PLAIN, 15));
			generationLabel.setText("Generation: ");
			// generationLabel.setHorizontalAlignment(SwingConstants.CENTER);
			this.generationLabel = generationLabel;
			generationFrame.add(generationLabel);
			// innerMainContent.add(generationLabel);

			// SECONDARY HEADER
			JPanel secondaryHeaderContent = new JPanel();
			secondaryHeaderContent.setOpaque(showBackgrounds);

			JLabel secondMVP = new JLabel();
			secondMVP.setFont(new Font("Showcard Gothic", Font.BOLD, 20));
			secondMVP
					.setText("<html><div style='color:#FF5BB2;'>S E C O N D&emsp;M V P</div></html>");
			secondaryHeaderContent.add(secondMVP);

			JLabel secondaryWinnerIcon = new JLabel();
			secondaryWinnerIcon.setIcon(new ImageIcon(loadedImages.get(ImageSet.BASE_DOODLEBUG)));
			secondaryWinnerIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
			this.secondaryWinnerIcon = secondaryWinnerIcon;

			JPanel secondaryCenterContent = new JPanel();
			secondaryCenterContent.setLayout(new FlowLayout(FlowLayout.CENTER));
			secondaryCenterContent.setOpaque(showBackgrounds);

			JLabel secondaryMVPLabel = new JLabel();
			secondaryMVPLabel.setForeground(Color.YELLOW);
			secondaryMVPLabel.setFont(new Font("Felix Titling", Font.BOLD, 10));
			secondaryMVPLabel.setText("MVP");
			secondaryMVPLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
			secondaryMVPLabel.setOpaque(showBackgrounds);

			JLabel secondaryWinnerTitle = new JLabel();
			secondaryWinnerTitle.setForeground(Color.GREEN);
			secondaryWinnerTitle.setFont(new Font("Copperplate Gothic Bold", Font.BOLD, 15));
			secondaryWinnerTitle.setText("Default");
			secondaryWinnerTitle.setHorizontalAlignment(SwingConstants.CENTER);
			this.secondaryWinnerTitle = secondaryWinnerTitle;

			secondaryCenterContent.add(secondaryWinnerTitle);

			JPanel secondaryMainContent = new JPanel();
			mainContent.setLayout(new BorderLayout());
			mainContent.setBackground(Color.RED);
			mainContent.setOpaque(showBackgrounds);

			JPanel secondaryTimeInSimFrame = new JPanel();
			secondaryTimeInSimFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
			secondaryTimeInSimFrame.setOpaque(showBackgrounds);

			JLabel secondaryTimeInSimLabel = new JLabel();
			secondaryTimeInSimLabel.setForeground(Color.WHITE);
			secondaryTimeInSimLabel.setFont(new Font("Courier New", Font.PLAIN, 15));
			secondaryTimeInSimLabel.setText("Time in Simulation: ");
			this.secondaryTimeInSimLabel = secondaryTimeInSimLabel;
			secondaryTimeInSimFrame.add(secondaryTimeInSimLabel);
			// innerMainContent.add(timeInSimLabel);

			JPanel secondaryAntsEatenFrame = new JPanel();
			secondaryAntsEatenFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
			secondaryAntsEatenFrame.setOpaque(showBackgrounds);

			JLabel secondaryAntsEatenLabel = new JLabel();
			secondaryAntsEatenLabel.setForeground(Color.WHITE);
			secondaryAntsEatenLabel.setFont(new Font("Courier New", Font.PLAIN, 15));
			secondaryAntsEatenLabel.setText("Ants Eaten: ");
			this.secondaryAntsEatenLabel = secondaryAntsEatenLabel;
			secondaryAntsEatenFrame.add(secondaryAntsEatenLabel);
			// innerMainContent.add(antsEatenLabel);

			JPanel secondaryGenerationFrame = new JPanel();
			secondaryGenerationFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
			secondaryGenerationFrame.setOpaque(showBackgrounds);

			JLabel secondaryGenerationLabel = new JLabel();
			secondaryGenerationLabel.setForeground(Color.WHITE);
			secondaryGenerationLabel.setFont(new Font("Courier New", Font.PLAIN, 15));
			secondaryGenerationLabel.setText("Generation: ");
			// generationLabel.setHorizontalAlignment(SwingConstants.CENTER);
			this.secondaryGenerationLabel = secondaryGenerationLabel;
			secondaryGenerationFrame.add(secondaryGenerationLabel);
			// innerMainContent.add(generationLabel);

			this.add(headerContent);
			this.add(Box.createVerticalStrut(15));
			this.add(winnerIcon);
			this.add(Box.createVerticalStrut(20));
			this.add(mvpLabel);
			this.add(centerContent);
			this.add(Box.createVerticalStrut(20));
			this.add(timeInSimFrame);
			this.add(antsEatenFrame);
			this.add(generationFrame);
			this.add(Box.createVerticalStrut(20));
			this.add(secondaryHeaderContent);
			this.add(Box.createVerticalStrut(15));
			this.add(secondaryWinnerIcon);
			this.add(Box.createVerticalStrut(20));
			this.add(secondaryMVPLabel);
			this.add(secondaryCenterContent);
			this.add(Box.createVerticalStrut(15));
			this.add(secondaryTimeInSimFrame);
			this.add(secondaryAntsEatenFrame);
			this.add(secondaryGenerationFrame);
			this.add(mainContent);
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
					mvpIcon = new ImageIcon(loadedImages.get(ImageSet.BASE_DOODLEBUG));
					this.antsEatenLabel
							.setText("<html>Ants Eaten: <span style='color:#bf00ff;'>"
									+ dbMVP.getAntsEatenMeter().getValue()
									+ "</span></html>");
					this.antsEatenLabel.setVisible(true);
					this.secondaryAntsEatenLabel.setVisible(false);
					this.secondaryWinnerIcon.setIcon(new ImageIcon(loadedImages.get(ImageSet.ANT_PROFILE)));
					this.secondaryWinnerTitle.setText("<html>" + antMVP.getName() + "</html>");
					this.secondaryGenerationLabel.setText(
							"<html>Generation: <span style='color:#bf00ff;'>" + antMVP.getGeneration()
									+ "</span></html>");
					this.secondaryTimeInSimLabel.setText("<html>Time in Simulation: <span style='color:#bf00ff;'>"
							+ Time.formatTime(antMVP.getTimeInSimulationInSeconds()) + "</span></html>");
				}
				case ANT -> {
					bugMVP = antMVP;
					mvpIcon = new ImageIcon(loadedImages.get(ImageSet.ANT_PROFILE));
					this.secondaryAntsEatenLabel
							.setText("<html>Ants Eaten: <span style='color:#bf00ff;'>"
									+ dbMVP.getAntsEatenMeter().getValue()
									+ "</span></html>");
					this.antsEatenLabel.setVisible(false);
					this.secondaryAntsEatenLabel.setVisible(true);
					this.secondaryWinnerIcon.setIcon(new ImageIcon(loadedImages.get(ImageSet.BASE_DOODLEBUG)));
					this.secondaryWinnerTitle.setText("<html>" + dbMVP.getName() + "</html>");
					this.secondaryGenerationLabel.setText(
							"<html>Generation: <span style='color:#bf00ff;'>" + dbMVP.getGeneration()
									+ "</span></html>");
					this.secondaryTimeInSimLabel.setText("<html>Time in Simulation: <span style='color:#bf00ff;'>"
							+ Time.formatTime(dbMVP.getTimeInSimulationInSeconds()) + "</span></html>");
				}
				case TITAN -> bugMVP = null;
				default -> {
					bugMVP = antMVP;
				}
			}

			this.winnerTitle.setText("<html>" + bugMVP.getName() + "</html>");
			this.winnerIcon.setIcon(mvpIcon);
			this.timeInSimLabel
					.setText("<html>Time in Simulation: <span style='color:#bf00ff;'>"
							+ Time.formatTime(bugMVP.getTimeInSimulationInSeconds()) + "</span></html>");
			this.generationLabel.setText(
					"<html>Generation: <span style='color:#bf00ff;'>" + bugMVP.getGeneration() + "</span></html>");
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
		title.setText("Life Sim");
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

				@Override
				public void mouseClicked(MouseEvent e) {
					Cell selectedCell = getCellFromScreenPoint(e.getPoint());
					Entity<?> entity = selectedCell.getOccupant();

					if (entity != null && !entity.hasTag()) {
						entity.setTag(true);
					}
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

	// public JPanel createContentRow() {

	// }

	public class EntityTag extends JPanel {

		private JLabel titleText;
		private JLabel paragraphText;

		public EntityTag() {
			this.setPreferredSize(new Dimension(50, 100));
			this.setOpaque(true);
			this.setBackground(Color.GREEN);

			// JPanel
		}
	}
}

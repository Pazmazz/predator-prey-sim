/*
 * @author Alex, Grier, Jaylen, Will
 * @written 3/28/2025
 * 
 * test
 * Project VSCode extensions:
 * - Java Extension Pack for Java v0.29.0
 * - vscode-icons
 * 
 * Project code formatter:
 * - Language Support for Java(TM) by Red Hat
 * 
 * Useful git commands:
 * - "git ls-files | xargs wc -l" - for counting collective lines of code in the 
 * 									project so far
 * 
 * "git ls-files | grep "\.java$" | xargs wc -l" -  for counting all lines of .java files
 * 													in the project so far
 * TODO:
 * - Make a custom event signal class
 */

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import classes.abstracts.Entity;
import classes.abstracts.RunService;
import classes.abstracts.Properties.Property;
import classes.entity.Ant;
import classes.entity.CellGrid.Cell;
import classes.entity.CellGrid.CellType;
import classes.entity.CellGrid;
import classes.entity.CellGrid.GridIntercept;
import classes.entity.Game;
import classes.entity.MainFrame;
import classes.entity.Unit2;
import classes.util.Console;
import classes.util.Console.DebugPriority;
import classes.util.Time;
import interfaces.Callback;
import classes.entity.Doodlebug;
import classes.entity.Vector2;
import classes.util.ObjectStream;
import java.util.Scanner;
import classes.entity.Titan;

/**
 * The entry-point file for the application
 */
@SuppressWarnings("unused")
public class App {

	public static void main(String[] args) {
		Game game = Game.getInstance();
		game.initConfig();
		game.initGameScreen();

		// Avg: ~0.01s
		// Console.benchmark("Render game grid", grid::toASCII);

		int chosenFPS = Console.promptMenu(
				"FPS Settings",
				"Choose FPS: ",
				1,
				new String[] {
						"60",
						"30",
						"10",
						"2",
						"1",
						"0.5",
						"0.25"
				});

		switch (chosenFPS) {
			case 1 -> game.setFPS(1.0 / 60);
			case 2 -> game.setFPS(1.0 / 30);
			case 3 -> game.setFPS(1.0 / 10);
			case 4 -> game.setFPS(1.0 / 2);
			case 5 -> game.setFPS(1.0);
			case 6 -> game.setFPS(2.0);
			case 7 -> game.setFPS(4.0);
		}

		int usePreload = Console.promptMenu(
				"Select Preload",
				"Preload: ",
				5,
				new String[] {
						"a mistake",
						"1v1",
						"red vs blue",
						"1v2",
						"none"
				});

		// Avg: ~0.001s
		Console.benchmark("Creating game grid", game::createGameGrid);

		switch (usePreload) {
			case 1 -> {
				game.getGameGrid().upload(
						"Cell{Unit2{4, 6}, Ant{}}, Cell{Unit2{5, 6}, Ant{}}, Cell{Unit2{6, 6}, Ant{}}, Cell{Unit2{7, 6}, Ant{}}, Cell{Unit2{8, 6}, Ant{}}, Cell{Unit2{9, 6}, Ant{}}, Cell{Unit2{10, 6}, Ant{}}, Cell{Unit2{11, 6}, Ant{}}, Cell{Unit2{12, 6}, Ant{}}, Cell{Unit2{13, 6}, Ant{}}, Cell{Unit2{14, 6}, Ant{}}, Cell{Unit2{15, 6}, Ant{}}, Cell{Unit2{16, 6}, Ant{}}, Cell{Unit2{17, 6}, Ant{}}, \r\n"
								+ //
								"\r\n" + //
								"Cell{Unit2{4, 5}, Ant{}}, Cell{Unit2{5, 5}, Ant{}}, Cell{Unit2{6, 5}, Ant{}}, Cell{Unit2{7, 5}, Ant{}}, Cell{Unit2{8, 5}, Ant{}}, Cell{Unit2{9, 5}, Ant{}}, Cell{Unit2{10, 5}, Ant{}}, Cell{Unit2{11, 5}, Ant{}}, Cell{Unit2{12, 5}, Ant{}}, Cell{Unit2{13, 5}, Ant{}}, Cell{Unit2{14, 5}, Ant{}}, Cell{Unit2{15, 5}, Ant{}}, Cell{Unit2{16, 5}, Ant{}}, Cell{Unit2{17, 5}, Ant{}}\r\n"
								+ //
								"\r\n" + //
								"\r\n" + //
								"\r\n" + //
								"Cell{Unit2{4, 11}, Ant{}}, Cell{Unit2{5, 11}, Ant{}}, Cell{Unit2{6, 11}, Ant{}}, Cell{Unit2{7, 11}, Ant{}}, Cell{Unit2{8, 11}, Ant{}}, Cell{Unit2{9, 11}, Ant{}}, Cell{Unit2{10, 11}, Ant{}}, Cell{Unit2{11, 11}, Ant{}}, Cell{Unit2{12, 11}, Ant{}}, Cell{Unit2{13, 11}, Ant{}}, Cell{Unit2{14, 11}, Ant{}}, Cell{Unit2{15, 11}, Ant{}}, Cell{Unit2{16, 11}, Ant{}}, Cell{Unit2{17, 11}, Ant{}}\r\n"
								+ //
								"\r\n" + //
								"Cell{Unit2{4, 12}, Ant{}}, Cell{Unit2{5, 12}, Ant{}}, Cell{Unit2{6, 12}, Ant{}}, Cell{Unit2{7, 12}, Ant{}}, Cell{Unit2{8, 12}, Ant{}}, Cell{Unit2{9, 12}, Ant{}}, Cell{Unit2{10, 12}, Ant{}}, Cell{Unit2{11, 12}, Ant{}}, Cell{Unit2{12, 12}, Ant{}}, Cell{Unit2{13, 12}, Ant{}}, Cell{Unit2{14, 12}, Ant{}}, Cell{Unit2{15, 12}, Ant{}}, Cell{Unit2{16, 12}, Ant{}}, Cell{Unit2{17, 12}, Ant{}}\r\n"
								+ //
								"\r\n" + //
								"Cell{Unit2{9, 7}, Ant{}}, Cell{Unit2{9, 8}, Ant{}}, Cell{Unit2{9, 9}, Ant{}}, Cell{Unit2{9, 10}, Ant{}}, Cell{Unit2{10, 7}, Ant{}}, Cell{Unit2{10, 8}, Ant{}}, Cell{Unit2{10, 9}, Ant{}}, Cell{Unit2{10, 10}, Ant{}}, Cell{Unit2{11, 7}, Ant{}},Cell{Unit2{11, 8}, Ant{}}, Cell{Unit2{11, 9}, Ant{}}, Cell{Unit2{11, 10}, Ant{}}\r\n"
								+ //
								"\r\n" + //
								"Cell{Unit2{7, 16}, Ant{}}, Cell{Unit2{7, 17}, Ant{}}, Cell{Unit2{8, 16}, Ant{}}, Cell{Unit2{8, 17}, Ant{}}, Cell{Unit2{9, 16}, Ant{}}, Cell{Unit2{9, 17}, Ant{}}, Cell{Unit2{10, 16}, Ant{}}, Cell{Unit2{10, 17}, Ant{}}, Cell{Unit2{11, 16}, Ant{}}, Cell{Unit2{11, 17}, Ant{}}, Cell{Unit2{12, 16}, Ant{}}, Cell{Unit2{12, 17}, Ant{}}, Cell{Unit2{13, 16}, Ant{}}, Cell{Unit2{13, 17}, Ant{}}, Cell{Unit2{14, 16}, Ant{}}, Cell{Unit2{14, 17}, Ant{}}, Cell{Unit2{15, 16}, Ant{}}, Cell{Unit2{15, 17}, Ant{}}, Cell{Unit2{16, 16}, Ant{}}, Cell{Unit2{16, 17}, Ant{}}, Cell{Unit2{17, 16}, Ant{}}, Cell{Unit2{17, 17}, Ant{}}\r\n"
								+ //
								"\r\n" + //
								"Cell{Unit2{4, 16}, Doodlebug{}}, Cell{Unit2{5, 16}, Doodlebug{}}, Cell{Unit2{4, 17}, Doodlebug{}}, Cell{Unit2{5, 17}, Doodlebug{}}");
			}
			case 2 -> {
				game.getGameGrid().upload("Cell{Unit2{2, 2}, Titan{}}, Cell{Unit2{19, 19}, Ant{}}");
			}
			case 3 -> {
				game.getGameGrid().upload(
						"Cell{Unit2{1, 1.0}, Titan{}}, Cell{Unit2{20, 1.0}, Ant{}}, Cell{Unit2{2, 1.0}, Titan{}}, Cell{Unit2{19, 1.0}, Ant{}}, Cell{Unit2{3, 1.0}, Titan{}}, Cell{Unit2{18, 1.0}, Ant{}}, Cell{Unit2{4, 1.0}, Titan{}}, Cell{Unit2{17, 1.0}, Ant{}}, Cell{Unit2{1, 2.0}, Titan{}}, Cell{Unit2{20, 2.0}, Ant{}}, Cell{Unit2{2, 2.0}, Titan{}}, Cell{Unit2{19, 2.0}, Ant{}}, Cell{Unit2{3, 2.0}, Titan{}}, Cell{Unit2{18, 2.0}, Ant{}}, Cell{Unit2{4, 2.0}, Titan{}}, Cell{Unit2{17, 2.0}, Ant{}}, Cell{Unit2{1, 3.0}, Titan{}}, Cell{Unit2{20, 3.0}, Ant{}}, Cell{Unit2{2, 3.0}, Titan{}}, Cell{Unit2{19, 3.0}, Ant{}}, Cell{Unit2{3, 3.0}, Titan{}}, Cell{Unit2{18, 3.0}, Ant{}}, Cell{Unit2{4, 3.0}, Titan{}}, Cell{Unit2{17, 3.0}, Ant{}}, Cell{Unit2{1, 4.0}, Titan{}}, Cell{Unit2{20, 4.0}, Ant{}}, Cell{Unit2{2, 4.0}, Titan{}}, Cell{Unit2{19, 4.0}, Ant{}}, Cell{Unit2{3, 4.0}, Titan{}}, Cell{Unit2{18, 4.0}, Ant{}}, Cell{Unit2{4, 4.0}, Titan{}}, Cell{Unit2{17, 4.0}, Ant{}}, Cell{Unit2{1, 5.0}, Titan{}}, Cell{Unit2{20, 5.0}, Ant{}}, Cell{Unit2{2, 5.0}, Titan{}}, Cell{Unit2{19, 5.0}, Ant{}}, Cell{Unit2{3, 5.0}, Titan{}}, Cell{Unit2{18, 5.0}, Ant{}}, Cell{Unit2{4, 5.0}, Titan{}}, Cell{Unit2{17, 5.0}, Ant{}}, Cell{Unit2{1, 6.0}, Titan{}}, Cell{Unit2{20, 6.0}, Ant{}}, Cell{Unit2{2, 6.0}, Titan{}}, Cell{Unit2{19, 6.0}, Ant{}}, Cell{Unit2{3, 6.0}, Titan{}}, Cell{Unit2{18, 6.0}, Ant{}}, Cell{Unit2{4, 6.0}, Titan{}}, Cell{Unit2{17, 6.0}, Ant{}}, Cell{Unit2{1, 7.0}, Titan{}}, Cell{Unit2{20, 7.0}, Ant{}}, Cell{Unit2{2, 7.0}, Titan{}}, Cell{Unit2{19, 7.0}, Ant{}}, Cell{Unit2{3, 7.0}, Titan{}}, Cell{Unit2{18, 7.0}, Ant{}}, Cell{Unit2{4, 7.0}, Titan{}}, Cell{Unit2{17, 7.0}, Ant{}}, Cell{Unit2{1, 8.0}, Titan{}}, Cell{Unit2{20, 8.0}, Ant{}}, Cell{Unit2{2, 8.0}, Titan{}}, Cell{Unit2{19, 8.0}, Ant{}}, Cell{Unit2{3, 8.0}, Titan{}}, Cell{Unit2{18, 8.0}, Ant{}}, Cell{Unit2{4, 8.0}, Titan{}}, Cell{Unit2{17, 8.0}, Ant{}}, Cell{Unit2{1, 9.0}, Titan{}}, Cell{Unit2{20, 9.0}, Ant{}}, Cell{Unit2{2, 9.0}, Titan{}}, Cell{Unit2{19, 9.0}, Ant{}}, Cell{Unit2{3, 9.0}, Titan{}}, Cell{Unit2{18, 9.0}, Ant{}}, Cell{Unit2{4, 9.0}, Titan{}}, Cell{Unit2{17, 9.0}, Ant{}}, Cell{Unit2{1, 10.0}, Titan{}}, Cell{Unit2{20, 10.0}, Ant{}}, Cell{Unit2{2, 10.0}, Titan{}}, Cell{Unit2{19, 10.0}, Ant{}}, Cell{Unit2{3, 10.0}, Titan{}}, Cell{Unit2{18, 10.0}, Ant{}}, Cell{Unit2{4, 10.0}, Titan{}}, Cell{Unit2{17, 10.0}, Ant{}}, Cell{Unit2{1, 11.0}, Titan{}}, Cell{Unit2{20, 11.0}, Ant{}}, Cell{Unit2{2, 11.0}, Titan{}}, Cell{Unit2{19, 11.0}, Ant{}}, Cell{Unit2{3, 11.0}, Titan{}}, Cell{Unit2{18, 11.0}, Ant{}}, Cell{Unit2{4, 11.0}, Titan{}}, Cell{Unit2{17, 11.0}, Ant{}}, Cell{Unit2{1, 12.0}, Titan{}}, Cell{Unit2{20, 12.0}, Ant{}}, Cell{Unit2{2, 12.0}, Titan{}}, Cell{Unit2{19, 12.0}, Ant{}}, Cell{Unit2{3, 12.0}, Titan{}}, Cell{Unit2{18, 12.0}, Ant{}}, Cell{Unit2{4, 12.0}, Titan{}}, Cell{Unit2{17, 12.0}, Ant{}}, Cell{Unit2{1, 13.0}, Titan{}}, Cell{Unit2{20, 13.0}, Ant{}}, Cell{Unit2{2, 13.0}, Titan{}}, Cell{Unit2{19, 13.0}, Ant{}}, Cell{Unit2{3, 13.0}, Titan{}}, Cell{Unit2{18, 13.0}, Ant{}}, Cell{Unit2{4, 13.0}, Titan{}}, Cell{Unit2{17, 13.0}, Ant{}}, Cell{Unit2{1, 14.0}, Titan{}}, Cell{Unit2{20, 14.0}, Ant{}}, Cell{Unit2{2, 14.0}, Titan{}}, Cell{Unit2{19, 14.0}, Ant{}}, Cell{Unit2{3, 14.0}, Titan{}}, Cell{Unit2{18, 14.0}, Ant{}}, Cell{Unit2{4, 14.0}, Titan{}}, Cell{Unit2{17, 14.0}, Ant{}}, Cell{Unit2{1, 15.0}, Titan{}}, Cell{Unit2{20, 15.0}, Ant{}}, Cell{Unit2{2, 15.0}, Titan{}}, Cell{Unit2{19, 15.0}, Ant{}}, Cell{Unit2{3, 15.0}, Titan{}}, Cell{Unit2{18, 15.0}, Ant{}}, Cell{Unit2{4, 15.0}, Titan{}}, Cell{Unit2{17, 15.0}, Ant{}}, Cell{Unit2{1, 16.0}, Titan{}}, Cell{Unit2{20, 16.0}, Ant{}}, Cell{Unit2{2, 16.0}, Titan{}}, Cell{Unit2{19, 16.0}, Ant{}}, Cell{Unit2{3, 16.0}, Titan{}}, Cell{Unit2{18, 16.0}, Ant{}}, Cell{Unit2{4, 16.0}, Titan{}}, Cell{Unit2{17, 16.0}, Ant{}}, Cell{Unit2{1, 17.0}, Titan{}}, Cell{Unit2{20, 17.0}, Ant{}}, Cell{Unit2{2, 17.0}, Titan{}}, Cell{Unit2{19, 17.0}, Ant{}}, Cell{Unit2{3, 17.0}, Titan{}}, Cell{Unit2{18, 17.0}, Ant{}}, Cell{Unit2{4, 17.0}, Titan{}}, Cell{Unit2{17, 17.0}, Ant{}}, Cell{Unit2{1, 18.0}, Titan{}}, Cell{Unit2{20, 18.0}, Ant{}}, Cell{Unit2{2, 18.0}, Titan{}}, Cell{Unit2{19, 18.0}, Ant{}}, Cell{Unit2{3, 18.0}, Titan{}}, Cell{Unit2{18, 18.0}, Ant{}}, Cell{Unit2{4, 18.0}, Titan{}}, Cell{Unit2{17, 18.0}, Ant{}}, Cell{Unit2{1, 19.0}, Titan{}}, Cell{Unit2{20, 19.0}, Ant{}}, Cell{Unit2{2, 19.0}, Titan{}}, Cell{Unit2{19, 19.0}, Ant{}}, Cell{Unit2{3, 19.0}, Titan{}}, Cell{Unit2{18, 19.0}, Ant{}}, Cell{Unit2{4, 19.0}, Titan{}}, Cell{Unit2{17, 19.0}, Ant{}}, Cell{Unit2{1, 20.0}, Titan{}}, Cell{Unit2{20, 20.0}, Ant{}}, Cell{Unit2{2, 20.0}, Titan{}}, Cell{Unit2{19, 20.0}, Ant{}}, Cell{Unit2{3, 20.0}, Titan{}}, Cell{Unit2{18, 20.0}, Ant{}}, Cell{Unit2{4, 20.0}, Titan{}}, Cell{Unit2{17, 20.0}, Ant{}}, Cell{Unit2{1, 21.0}, Titan{}}, Cell{Unit2{20, 21.0}, Ant{}}, Cell{Unit2{2, 21.0}, Titan{}}, Cell{Unit2{19, 21.0}, Ant{}}, Cell{Unit2{3, 21.0}, Titan{}}, Cell{Unit2{18, 21.0}, Ant{}}, Cell{Unit2{4, 21.0}, Titan{}}, Cell{Unit2{17, 21.0}, Ant{}}, Cell{Unit2{1, 22.0}, Titan{}}, Cell{Unit2{20, 22.0}, Ant{}}, Cell{Unit2{2, 22.0}, Titan{}}, Cell{Unit2{19, 22.0}, Ant{}}, Cell{Unit2{3, 22.0}, Titan{}}, Cell{Unit2{18, 22.0}, Ant{}}, Cell{Unit2{4, 22.0}, Titan{}}, Cell{Unit2{17, 22.0}, Ant{}}, Cell{Unit2{1, 23.0}, Titan{}}, Cell{Unit2{20, 23.0}, Ant{}}, Cell{Unit2{2, 23.0}, Titan{}}, Cell{Unit2{19, 23.0}, Ant{}}, Cell{Unit2{3, 23.0}, Titan{}}, Cell{Unit2{18, 23.0}, Ant{}}, Cell{Unit2{4, 23.0}, Titan{}}, Cell{Unit2{17, 23.0}, Ant{}}, Cell{Unit2{1, 24.0}, Titan{}}, Cell{Unit2{20, 24.0}, Ant{}}, Cell{Unit2{2, 24.0}, Titan{}}, Cell{Unit2{19, 24.0}, Ant{}}, Cell{Unit2{3, 24.0}, Titan{}}, Cell{Unit2{18, 24.0}, Ant{}}, Cell{Unit2{4, 24.0}, Titan{}}, Cell{Unit2{17, 24.0}, Ant{}}, Cell{Unit2{1, 25.0}, Titan{}}, Cell{Unit2{20, 25.0}, Ant{}}, Cell{Unit2{2, 25.0}, Titan{}}, Cell{Unit2{19, 25.0}, Ant{}}, Cell{Unit2{3, 25.0}, Titan{}}, Cell{Unit2{18, 25.0}, Ant{}}, Cell{Unit2{4, 25.0}, Titan{}}, Cell{Unit2{17, 25.0}, Ant{}}");
			}
			case 4 -> {
				game.getGameGrid()
						.upload("Cell{Unit2{2, 10}, Titan{}}, Cell{Unit2{19, 19}, Ant{}}, Cell{Unit2{19, 2}, Ant{}}");
			}
			case 5 -> {
				int spawnTitan = Console.promptMenu(
						"Spawn a Titan Ant?",
						"Carefully decide: ",
						1,
						new String[] {
								"Sure, why not",
								"No, I want to keep my FPS"
						});

				switch (spawnTitan) {
					case 1 -> game.getSettings().setTitanSpawn(true);
				}

				int setAnts = Integer.parseInt(Console.promptMessage("Initial Ants: ", "100"));
				int setDoodlebugs = Integer.parseInt(Console.promptMessage("Initial Doodlebugs: ", "5"));

				game.getSettings().setInitialAnts(setAnts);
				game.getSettings().setInitialDoodlebugs(setDoodlebugs);

				Console.benchmark("Initializing game grid", game::initGameGrid);
			}
		}

		// Avg: ~0.02s
		// Console.benchmark("Initializing game grid", game::initGameGrid);

		// Avg: ~0.005s
		Console.benchmark("Initializing RunService", game::initRunService);

		// Avg: ~0.3s
		// Console.benchmark("Initializing game screen", game::initGameScreen);

		CellGrid grid = game.getGameGrid();

		if (game.getSettings().canSpawnTitan() && usePreload == 5) {
			grid.populate();
			Cell titanCell = grid.getRandomAvailableCell();

			if (titanCell != null) {
				titanCell.setOccupant(new Titan());
				grid.getRandomAvailableCell().setOccupant(new Titan());
				grid.getRandomAvailableCell().setOccupant(new Titan());
			} else {
				throw new Error("Titan could not spawn (no available cells left)");
			}

			// grid.getCell(new Unit2(1, 1)).setPathCell();
			// Cell c0 = grid.getCell(new Unit2(2, 2));
			// c0.setPathCell();
			// Console.println(c0.getUnit2Center());
		}

		new Thread(() -> {
			Console.println("");
			for (int i = 3; i > 0; i--) {
				Console.println("$text-bright_green STARTING IN... $text-reset " + i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			Console.println("\n");
			Console.benchmark("Render game grid", grid::toASCII);
			Console.promptMessage("Press enter to START", "");
			game.start();
		}).start();

		// Console.println(grid.download());
	}
}

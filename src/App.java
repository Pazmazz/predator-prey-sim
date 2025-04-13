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

import classes.abstracts.Entity;
import classes.abstracts.RunService;
import classes.abstracts.Properties.Property;
import classes.entity.Ant;
import classes.entity.CellGrid.Cell;
import classes.entity.CellGrid.CellType;
import classes.entity.CellGrid;
import classes.entity.CellGrid.GridIntercept;
import classes.entity.Game;
import classes.entity.Unit2;
import classes.util.Console;
import classes.util.Console.DebugPriority;
import classes.util.Time;
import interfaces.Callback;
import classes.entity.Doodlebug;
import classes.entity.Vector2;
import classes.util.ObjectStream;
import java.util.Scanner;

/**
 * The entry-point file for the application
 */
public class App {

	public static void main(String[] args) {
		Game game = Game.getInstance();

		// Avg: ~0.004s
		Console.benchmark("Game config", game::initConfig);

		// Avg: ~0.001s
		Console.benchmark("Creating game grid", game::createGameGrid);

		// Avg: ~0.02s
		Console.benchmark("Initializing game grid", game::initGameGrid);

		// Avg: ~0.005s
		Console.benchmark("Initializing RunService", game::initRunService);

		// Avg: ~0.3s
		// Console.benchmark("Initializing game screen", game::initGameScreen);

		CellGrid grid = game.getGameGrid();

		// grid.upload(
		// "Cell{Unit2{9, 1}, Ant{}}, Cell{Unit2{15, 3}, Ant{}}, Cell{Unit2{5, 8},
		// Ant{}}, Cell{Unit2{1, 19}, Doodlebug{}}, Cell{Unit2{7, 15}, Doodlebug{}},
		// Cell{Unit2{12, 3}, Ant{}}, Cell{Unit2{6, 1}, Doodlebug{}}, Cell{Unit2{3, 14},
		// Doodlebug{}}, Cell{Unit2{18, 6}, Ant{}}, Cell{Unit2{9, 18}, Doodlebug{}}");

		// Avg: ~0.01s
		// Console.benchmark("Render game grid", grid::toASCII);

		int menuItem = Console.promptMenu(
				"Game Settings",
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

		switch (menuItem) {
			case 1 -> game.setFPS(1.0 / 60);
			case 2 -> game.setFPS(1.0 / 30);
			case 3 -> game.setFPS(1.0 / 10);
			case 4 -> game.setFPS(1.0 / 2);
			case 5 -> game.setFPS(1.0);
			case 6 -> game.setFPS(2.0);
			case 7 -> game.setFPS(4.0);
		}

		boolean usePreload = Console.promptBoolean("Use preload", false);

		if (usePreload) {
			grid.upload(
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

		new Thread(() -> {
			Console.println("");
			for (int i = 3; i > 0; i--) {
				Console.println("$text-bright_purple STARTING IN... " + i);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}
			}
			Console.benchmark("Render game grid", grid::toASCII);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
			}
			game.start();
		}).start();

		// Console.println(grid.download());
	}
}

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import classes.abstracts.Entity;
import classes.abstracts.Properties.Property;
import classes.entity.Ant;
import classes.entity.CellGrid.Cell;
import classes.entity.CellGrid.CellType;
import classes.entity.CellGrid;
import classes.entity.CellGrid.GridIntercept;
import classes.entity.ValueMeter.RESET_TYPE;
import classes.entity.Game;
import classes.entity.GameScreen;
import classes.entity.Unit2;
import classes.entity.ValueMeter;
import classes.util.Console;
import classes.util.Math2;
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

	public static Game game = Game.getInstance();

	public static void main(String[] args) {
		game.initConfig();

		int resolutionChoice = Console.promptMenu(
				"Select Window Resolution",
				"Select: ",
				1, new String[] {
						"700px",
						"800px",
						"900px",
				});

		switch (resolutionChoice) {
			case 1 -> game.getSettings().setScreenWidth(700);
			case 2 -> game.getSettings().setScreenWidth(800);
			case 3 -> game.getSettings().setScreenWidth(900);
		}

		Console.br();
		String cellCount = Console.promptMessage("Enter grid resolution: ", "20");
		int parsedCellCount = Integer.parseInt(cellCount);
		game.getSettings().setGridSize(new Unit2(parsedCellCount, parsedCellCount));

		Console.br();
		String initialAntCount = Console.promptMessage("Enter initial Ant count: ", "100");
		game.getSettings().setInitialAnts(Integer.parseInt(initialAntCount));

		Console.br();
		String initialDBCount = Console.promptMessage("Enter initial Doodlebug count: ", "5");
		game.getSettings().setInitialDoodlebugs(Integer.parseInt(initialDBCount));

		Console.br();
		boolean renderASCII = Console.promptBoolean("Render ASCII?", false);

		if (renderASCII) {
			game.getSettings().setRenderASCII(true);
		}

		game.boot();
		game.start();
	}
}

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
import classes.abstracts.Properties.Property;
import classes.entity.Ant;
import classes.entity.CellGrid.Cell;
import classes.entity.CellGrid.CellType;
import classes.entity.CellGrid;
import classes.entity.CellGrid.GridIntercept;
import classes.entity.ValueMeter.RESET_TYPE;
import classes.entity.Game;
import classes.entity.ScreenTest;
import classes.entity.Unit2;
import classes.entity.ValueMeter;
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

	public static Game game = Game.getInstance();

	public static void main(String[] args) {

		game.initConfig();
		new ScreenTest();

		// game.boot();
		// new Thread(() -> {
		// try {
		// Thread.sleep(1000);
		// game.start();
		// } catch (Exception e) {
		// }
		// }).start();
	}
}

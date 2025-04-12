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

import java.util.ArrayList;
import java.util.HashMap;

import classes.abstracts.Entity;
import classes.abstracts.RunService;
import classes.abstracts.Properties.Property;
import classes.entity.Ant;
import classes.entity.CellGrid.Cell;
import classes.entity.CellGrid;
import classes.entity.CellGrid.GridIntercept;
import classes.entity.Game;
import classes.entity.Unit2;
import classes.util.Console;
import classes.util.FileManager;
import classes.util.Console.DebugPriority;
import classes.util.Time;
import interfaces.Callback;
import classes.entity.Doodlebug;
import classes.entity.Vector2;

/**
 * The entry-point file for the application
 */
public class App {

	public static void main(String[] args) {
		Vector2 v = new Vector2(5, 5);
		Unit2 u = new Unit2(6, 6);

		// Console.benchmark("toString()", App::tryToString);
		// Console.benchmark("serialize()", App::trySerialize);
	}

	// public static String tryToString() {
	// Vector2 v = new Vector2(5, 5);
	// for (int i = 0; i < 500; i++) {
	// v.toString();
	// }
	// return "vector2";
	// }

	// public static String trySerialize() {
	// Vector2 v = new Vector2(5, 5);
	// for (int i = 0; i < 500; i++) {
	// v.serialize();
	// }
	// return "serialize";
	// }
}

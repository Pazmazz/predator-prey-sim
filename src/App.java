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

		Console.benchmark("Render game grid", grid::toASCII); // Avg: ~0.01s
		// game.start();

		String serialized = grid.getCell(new Unit2(6, 6)).serialize();
		// Console.println(game.deserialize(serialized));
		// Game.tokenize("asd{x, y{w, r, t}, z}");
		Game.tokenize("g{1}");
		// Console.println(Game.testTokens);
		// Console.println("outer: " + Game.findLastClosing("hello {sese{}}", 6, '{',
		// '}'));
		// Cell.deserialize(serialized);

		// try {
		// Constructor<?> constructor =
		// Class.forName("classes.entity.Unit2").getDeclaredConstructor();
		// Console.println(Class.forName("classes.entity.Unit2").getDeclaredConstructor().newInstance(new
		// int[] {3, 4}));
		// } catch (ClassNotFoundException e) {
		// Console.println(e);
		// } catch (NoSuchMethodException e) {
		// } catch (InstantiationException e) {

		// } catch (IllegalAccessException e) {

		// } catch (InvocationTargetException e) {

		// }
	}
}

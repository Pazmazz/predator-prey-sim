/*
 * @written 4/12/2025
 */
package classes.util;

import java.util.ArrayList;

import classes.abstracts.Entity;
import classes.entity.Ant;
import classes.entity.CellGrid.Cell;
import classes.entity.Doodlebug;
import classes.entity.Game;
import classes.entity.Unit2;
import classes.entity.Vector2;

/**
 * This class is responsible for anything that has to do with serializing or
 * deserializing object streams from or into their concrete data form. Get
 * ready, you're about to see some ugly ass code.
 */
public class ObjectStream {

	final private static Game game = Game.getInstance();

	/**
	 * Creates a new instance from a given class name. Class names are hard-coded
	 * for now unfortunately, so options are limited and the code is pretty ugly.
	 * 
	 * @param className the class to deserialize
	 * @param data      the object's construction parameters (not necessarily the
	 *                  object's constructor method parameters)
	 * @return a new class instance with the given instantiation data
	 */
	private static Object newInstanceFromClass(String className, ArrayList<Object> data) {
		Object obj;

		switch (className) {
			case "Cell" -> {
				Unit2 unit2 = (Unit2) data.get(0);
				Object _entity = data.get(1);
				Cell cell = (Cell) game.getGameGrid().getCell(unit2, false);

				if (_entity instanceof Entity<?>) {
					Entity<?> entity = (Entity<?>) _entity;
					entity.assignCell(cell);
				} else {
					// Console.println("Entity: ", _entity);
				}
				obj = cell;
			}
			case "Unit2" -> {
				obj = new Unit2(
						(int) Double.parseDouble((String) data.get(0)),
						(int) Double.parseDouble((String) data.get(1)));
			}
			case "Vector2" -> {
				obj = new Vector2(
						Double.parseDouble((String) data.get(0)),
						Double.parseDouble((String) data.get(1)));
			}
			case "Ant" -> {
				obj = new Ant();
			}
			case "Doodlebug" -> {
				obj = new Doodlebug();
			}
			default -> obj = null;
		}

		return obj;
	}

	/**
	 * A standalone method for computing the last index position of some outer-most
	 * brackets, defined by {@code open} and {@code close}
	 * 
	 * @param str   the string to find the final outer-most closing bracket in
	 * @param start the starting index to search at in the string
	 * @param open  the open bracket character
	 * @param close the close bracket character
	 * 
	 * @return the index position in the string of the final closing outer-most
	 *         bracket
	 */
	private static int findLastClosing(StringBuilder str, int start, char open, char close) {
		int stack = -1;
		int len = str.length();

		for (int index = start; index < len; index++) {
			char token = str.charAt(index);
			if (token == open) {
				if (stack == -1)
					stack = 1;
				else
					stack++;
			} else if (token == close && stack > 0) {
				stack--;
				if (stack == 0)
					return index;
			}
		}
		return stack > 0
				? -1
				: stack;
	}

	/**
	 * Deserializes an object stream. Object streams are of the form:
	 * {@code "Object&#123;data0, data1, ...&#125;"}
	 * 
	 * @param data the serialized data to deserialize
	 * @return the deserialized concrete data
	 * 
	 * @see #deserialize(String data)
	 */
	public static ArrayList<Object> deserialize(String data) {
		int cursor = 0;
		int len = data.length();
		StringBuilder objBuffer = new StringBuilder();
		StringBuilder dataBuffer = new StringBuilder(data);
		ArrayList<Object> set = new ArrayList<>();

		while (cursor < len) {
			char token = dataBuffer.charAt(cursor);
			switch (token) {
				case '{' -> {
					String obj = objBuffer.toString().trim();
					objBuffer.setLength(0);
					int close = findLastClosing(dataBuffer, cursor, '{', '}');
					if (close == -1)
						throw new Error("Parsing error: No closing bracket found at " + cursor);

					Object inst = newInstanceFromClass(
							obj,
							deserialize(dataBuffer.substring(cursor + 1, close)));
					set.add(inst);
					cursor = close + 1;
				}
				case ',' -> {
					String obj = objBuffer.toString().trim();
					objBuffer.setLength(0);
					set.add(obj);
				}
				default -> objBuffer.append(token);
			}
			cursor++;
		}

		if (objBuffer.length() > 0) {
			String obj = objBuffer.toString().trim();
			objBuffer.setLength(0);
			set.add(obj);
		}
		return set;
	}
}

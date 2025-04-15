package test;

import java.util.ArrayList;
import java.util.HashMap;

import classes.abstracts.Entity;
import classes.entity.Ant;
import classes.entity.CellGrid.Cell;
import classes.entity.Doodlebug;
import classes.entity.Game;
import classes.entity.Titan;
import classes.entity.Unit2;
import classes.entity.Vector2;

@SuppressWarnings("unused")
public class Parser {
	final private static Game game = Game.getInstance();

	@SuppressWarnings("unchecked")
	private static Object newInstanceFromClass(String className, Object data) {
		Object obj = null;

		if (data instanceof HashMap) {
			data = (HashMap<String, Object>) data;
		} else if (data instanceof ArrayList) {
			data = (ArrayList<Object>) data;
		}

		// switch (className) {
		// case "Unit2" -> obj = new Unit2(data)
		// case "Vector2" -> obj = new Vector2(data)
		// }

		return obj;
	}

	public static int findLastClosing(StringBuilder str, int start, char open, char close) {
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

	public static ArrayList<Object> deserializeObject(String data) {
		StringBuilder dataBuffer = new StringBuilder(data);
		StringBuilder objBuffer = new StringBuilder();
		ArrayList<Object> set = new ArrayList<>();

		int cursor = 0;
		int len = data.length();
		boolean stringGateOpen = false;
		boolean writingToMap = false;
		boolean escapeNext = false;

		while (cursor < len) {
			char token = dataBuffer.charAt(cursor);
			if ((token == '{'
					|| token == '='
					|| token == ',')
					&& !stringGateOpen) {
				String objStream = objBuffer.toString();
				objBuffer.setLength(0);

				// obj can be a Class instance, ArrayList, HashMap, or primitive
				Object obj = null;

				switch (token) {
					case '{' -> {
						int close = findLastClosing(dataBuffer, cursor, '{', '}');
						if (close == -1)
							throw new Error("Parsing error: No closing bracket found at " + cursor);

						String content = dataBuffer.substring(cursor + 1, close);
						cursor = close;
						obj = deserializeObject(content);

						// if we're dealing with a class, pass the resulting obj to it
						if (objStream.length() > 0) {
							obj = newInstanceFromClass(objStream, obj);
						}

						System.out.println("Object: \"" + objStream + "\"");
						System.out.println("Content: \"" + content + "\"");
					}
					case '=' -> {

					}
					case '\\' -> {
						escapeNext = true;
					}
					// case ',' -> {
					// obj = "";
					// }
				}
				set.add(obj);
			} else if (stringGateOpen && token == '"') {
				stringGateOpen = false;
			}
			objBuffer.append(token);
			cursor++;
		}

		return set;
	}
}

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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import test.Parser;

/**
 * The entry-point file for the application
 */
public class App {

	public static void main(String[] args) {

		// String hashmap = "{ a=b, b=Cell(1,2,3), c=[x,y,z], g={x=1, y=2, z=3} }";
		String list = "[1,[6,5,4],3, ]";

		String hashmap = "{\"key\":ghj,\"asd\":df}";

		HashMap<String, Object> m = deserialize(hashmap);
		ArrayList<Object> l = deserialize(list);
		System.out.println(m);
		System.out.println(l);

	}

	public static Object getObj(String str) {
		Object obj;

		switch (str) {
			case "Unit2" -> obj = new Object();
			default -> obj = null;
		}

		return obj;
	}

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String data) {
		data = data.trim();
		Stack<Object> stack = new Stack<>();
		StringBuilder keyBuffer = new StringBuilder();
		StringBuilder valueBuffer = new StringBuilder();
		StringBuilder currentBuffer;

		int len = data.length();
		boolean writingString = false;
		T root;

		char firstChar = data.charAt(0);
		char lastChar = data.charAt(len - 1);
		if (!(firstChar == '{' && lastChar == '}' || firstChar == '[' && lastChar == ']'))
			throw new Error("Parsing error: No initial data structure was provided");
		else if (firstChar == '{') {
			currentBuffer = keyBuffer;
			root = (T) stack.push(new HashMap<String, Object>());
		} else {
			currentBuffer = valueBuffer;
			root = (T) stack.push(new ArrayList<Object>());
		}

		for (int index = 1; index < len; index++) {
			char token = data.charAt(index);
			Object currentStack = stack.peek();
			boolean isMap = currentStack instanceof HashMap;
			boolean isArray = currentStack instanceof ArrayList;
			if (writingString) {
				if (token == '"')
					writingString = false;
				else
					currentBuffer.append(token);
				continue;
			} else if (Character.isWhitespace(token))
				continue;
			else if (currentBuffer == valueBuffer) {
				Object value = null;
				switch (token) {
					case '"' -> {
						writingString = true;
						continue;
					}
					case '{' -> value = stack.push(new HashMap<String, Object>());
					case '[' -> value = stack.push(new ArrayList<Object>());
					case ',', '}', ']' -> {
						value = valueBuffer.toString();
						if (token == '}' || token == ']') {
							stack.pop();
						}
					}
					default -> {
						valueBuffer.append(token);
						continue;
					}
				}
				valueBuffer.setLength(0);
				if (isMap) {
					System.out.println("Adding entry \"" + keyBuffer.toString() + "\" -> \"" + value + "\"");
					((HashMap<String, Object>) currentStack).put(
							keyBuffer.toString(),
							value);
					keyBuffer.setLength(0);
					currentBuffer = keyBuffer;
				} else if (isArray)
					((ArrayList<Object>) currentStack).add(value);
				continue;
			}
			if (isMap) {
				if (keyBuffer.isEmpty()) {
					if (token == '"') {
						writingString = true;
						currentBuffer = keyBuffer;
					} else
						throw new Error("Parsing error: HashMap keys must be strings (key \""
								+ keyBuffer.toString()
								+ "\" is invalid)");
				} else if (valueBuffer.isEmpty()) {
					if (token == ':')
						currentBuffer = valueBuffer;
					else
						throw new Error("Parsing error: Expected \":\" after key \""
								+ keyBuffer.toString()
								+ "\"");
				}
				// Current stack is ArrayList
			} else if (valueBuffer.isEmpty()) {
				currentBuffer = valueBuffer;
			}
		}

		return root;
	}
}

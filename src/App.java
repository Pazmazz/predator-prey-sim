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

		String hashmap = "{ a=b, b=Cell(1,2,3), c=[x,y,z], g={x=1, y=2, z=3} }";
		String list = "[ x, y, 2, 3, Cell(1,2,3), [4,5], {x=1, y=2, z=3} ]";

		// ArrayList<Object> listObj = deserialize(list);
		// HashMap<String, Object> hashmapObj = deserialize(hashmap);
		String s = "";
		Class<String> g = String.class;

		ArrayList<Object> t = deserialize(list);
		HashMap<String, Object> m = deserialize(list);

		String a = "hello\\\"world\\\"";

	}

	public static Object getObj(String str) {
		Object obj;

		switch (str) {
			case "Unit2" -> obj = new Object();
			default -> obj = null;
		}

		return obj;
	}

	// @SuppressWarnings("unchecked")
	// public static <T> T deserialize(String data) {
	// Object collection;

	// if (data.length() == 0) {
	// collection = new HashMap<>();
	// } else {
	// collection = new ArrayList<>();
	// }

	// return (T) collection;
	// }

	@SuppressWarnings("unchecked")
	public static <T> T deserialize(String data) {
		Stack<Object> stack = new Stack<>();
		StringBuilder keyBuffer = new StringBuilder();
		StringBuilder valueBuffer = new StringBuilder();
		StringBuilder currentBuffer;
		boolean writingString = false;

		data = data.trim();
		char firstChar = data.charAt(0);
		int len = data.length();

		if (firstChar == '{') {
			stack.push(new HashMap<String, Object>());
			currentBuffer = keyBuffer;
		} else if (firstChar == '[') {
			stack.push(new ArrayList<Object>());
			currentBuffer = valueBuffer;
		} else
			throw new Error("Parsing error: Invalid JON structure (expected brackets, got \"" + firstChar + "\"");

		for (int index = 1; index < len; index++) {
			char token = data.charAt(index);
			if (writingString) {
				if (token == '"')
					writingString = false;
				else
					currentBuffer.append(token);
				continue;
			} else if (currentBuffer == valueBuffer) {
				switch (token) {
					case '"' -> writingString = true;
					case '{' -> stack.push(new HashMap<String, Object>());
					case '[' -> stack.push(new ArrayList<Object>());
				}
				continue;
			}
			if (Character.isWhitespace(token))
				continue;
			if (stack.peek() instanceof HashMap) {
				if (keyBuffer.isEmpty()) {
					if (token == '"') {
						writingString = true;
						currentBuffer = keyBuffer;
					} else
						throw new Error("Parsing error: HashMap keys must be strings");
				} else if (token == ':') {
					currentBuffer = valueBuffer;
				} else
					throw new Error("Parsing error: Expected \":\" after key");
			}
		}

		return (T) stack.peek();
	}
}

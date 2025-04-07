/*
 * @written 3/29/2025
 */
package classes.util;

import classes.abstracts.Application;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Used for interacting with and styling the console. Much of this code was
 * reused from the final project from the previous semester in CS-190.
 */
public class Console extends Application {

	//
	// Debug info (immutable at runtime)
	//
	final private static Map<DebugPriority, String> debugPrefixes = new HashMap<>() {
		{
			put(DebugPriority.LOW, "$text-green [Debug Low]$text-reset ");
			put(DebugPriority.MEDIUM, "$text-purple [Debug Medium]$text-reset ");
			put(DebugPriority.HIGH, "$text-red [Debug High]$text-reset ");
		}
	};

	final private static Map<DebugPriority, Boolean> listeningDebugPriorities = new HashMap<>() {
		{
			put(DebugPriority.LOW, Boolean.TRUE);
			put(DebugPriority.MEDIUM, Boolean.TRUE);
			put(DebugPriority.HIGH, Boolean.TRUE);
		}
	};

	private static boolean debugModeEnabled = false;
	private static boolean consoleColorsEnabled = false;

	public static enum DebugPriority {
		LOW,
		MEDIUM,
		HIGH
	}

	final private static String COLOR_TAG_PATTERN = "\\$([a-zA-Z]+)\\-?([a-zA-Z_]*)";
	final private static String[][] DEFAULT_THEME = {
			{ "error", "$bg-black", "$text-white" },
			{ "warn", "$bg-blue", "$text-black" }
	};

	final private static String[][] TEXT_COLORS = {
			{ "reset", "\u001B[0m" },
			{ "black", "\u001B[30m" },
			{ "red", "\u001B[31m" },
			{ "green", "\u001B[32m" },
			{ "yellow", "\u001B[33m" },
			{ "blue", "\u001B[34m" },
			{ "purple", "\u001B[35m" },
			{ "cyan", "\u001B[36m" },
			{ "white", "\u001B[37m" },
			{ "bright_black", "\u001B[90m" },
			{ "bright_red", "\u001B[91m" },
			{ "bright_green", "\u001B[92m" },
			{ "bright_yellow", "\u001B[93m" },
			{ "bright_blue", "\u001B[94m" },
			{ "bright_purple", "\u001B[95m" },
			{ "bright_cyan", "\u001B[96m" },
			{ "bright_white", "\u001B[97m" }, };

	final private static String[][] BG_COLORS = {
			{ "black", "\u001B[40m" },
			{ "red", "\u001B[41m" },
			{ "green", "\u001B[42m" },
			{ "yellow", "\u001B[43m" },
			{ "blue", "\u001B[44m" },
			{ "purple", "\u001B[45m" },
			{ "cyan", "\u001B[46m" },
			{ "white", "\u001B[47m" },
			{ "bright_black", "\u001B[100m" },
			{ "bright_red", "\u001B[101m" },
			{ "bright_green", "\u001B[102m" },
			{ "bright_yellow", "\u001B[103m" },
			{ "bright_blue", "\u001B[104m" },
			{ "bright_purple", "\u001B[105m" },
			{ "bright_cyan", "\u001B[106m" },
			{ "bright_white", "\u001B[107m" }
	};

	/**
	 * Internally calls {@code System.out.println} but applies conditional
	 * console colors.
	 *
	 * @param contents the collection of objects to print
	 */
	public static void println(Object... contents) {
		if (consoleColorsEnabled) {
			System.out.println(substituteColors(Formatter.concatArray(contents)));
		} else {
			System.out.println(replaceColorTags(Formatter.concatArray(contents)));
		}
	}

	/**
	 * Internally calls {@code System.out.print} but applies conditional console
	 * colors
	 *
	 * @param message the message to inline print
	 */
	public static void print(String message) {
		if (consoleColorsEnabled) {
			System.out.print(substituteColors(message));
		} else {
			System.out.print(replaceColorTags(message));
		}
	}

	/**
	 * Throws a generic unchecked error
	 *
	 * @param contents the objects to include in the error
	 */
	public static void error(Object... contents) {
		throw new Error(Formatter.concatArray(contents));
	}

	/**
	 * Prints an ASCII line to the console of a specified length
	 *
	 * @param repeat
	 */
	public static void br(int repeat) {
		println("-".repeat(repeat));
	}

	/**
	 * Override: {@code br}
	 *
	 * Calls the root method but passes a default length value of {@code 50}
	 */
	public static void br() {
		br(50);
	}

	/**
	 * Checks if the console is currently in debug mode
	 *
	 * @return true if the console is in debug mode
	 */
	public static boolean isDebugMode() {
		return debugModeEnabled;
	}

	/**
	 * Sets the console's debug mode to either enabled or disabled
	 *
	 * @param enabled whether debug mode is enabled or disabled (true = enabled)
	 */
	public static void setDebugModeEnabled(boolean enabled) {
		debugModeEnabled = enabled;
	}

	/**
	 * Disables a specified {@code DebugPriority} level from being printed to
	 * the output window.
	 *
	 * @param priority the {@code DebugPriority} level to hide
	 */
	public static void hideDebugPriority(DebugPriority priority) {
		listeningDebugPriorities.put(priority, Boolean.FALSE);
	}

	/**
	 * Enables a specified {@code DebugPriority} level to being printed to the
	 * output window.
	 *
	 * @param priority the {@code DebugPriority} level to show
	 */
	public static void showDebugPriority(DebugPriority priority) {
		listeningDebugPriorities.put(priority, Boolean.TRUE);
	}

	/**
	 * Checks whether a specified {@code DebugPriority} is currently enabled
	 *
	 * @param priority the {@code DebugPriority} to check for
	 * @return true if the console has the specified {@code DebugPriority}
	 *         enabled
	 */
	public static boolean isShowingDebugPriority(DebugPriority priority) {
		return listeningDebugPriorities.get(priority);
	}

	/**
	 * Sets a {@code DebugPriority} to be exclusively shown, disabling all the
	 * other priority levels.
	 *
	 * @param priority the {@code DebugPriority} to exlusively show
	 */
	public static void setDebugPriority(DebugPriority priority) {
		for (DebugPriority key : listeningDebugPriorities.keySet()) {
			if (priority == key) {
				listeningDebugPriorities.put(key, Boolean.TRUE);
			} else {
				listeningDebugPriorities.put(key, Boolean.FALSE);
			}
		}
	}

	/**
	 * Wraps around {@code Console.println} to conditionally execute only IF
	 * {@code debugModeEnabled} is true AND the specified {@code DebugPriority}
	 * level is enabled.
	 *
	 * @param priority the {@code DebugPriority} level to set the print message
	 *                 to
	 * @param messages the object messages to be printed
	 */
	public static void debugPrint(DebugPriority priority, Object... messages) {
		if (isDebugMode() && isShowingDebugPriority(priority)) {
			println(String.format(
					"%s: %s",
					debugPrefixes.get(priority),
					Formatter.concatArray(messages, " | ")));
		}
	}

	/**
	 * Wraps around {@code Console.println} to conditionally execute only IF
	 * {@code debugModeEnabled} is true AND the specified {@code DebugPriority}
	 * level is enabled.
	 *
	 * @param priority the {@code DebugPriority} level to set the print message
	 *                 to
	 * @param messages the object messages to be printed
	 */
	public static void debugPrint(Object... messages) {
		debugPrint(DebugPriority.LOW, messages);
	}

	/**
	 * Sets console colors to be enabled or disabled.
	 *
	 * <p>
	 * Its worth noting that on some systems, the character codes for rendering
	 * console colors are not supported, hence the setting.
	 *
	 * @param enabled whether console colors are enabled or disabled (true =
	 *                enabled)
	 */
	public static void setConsoleColorsEnabled(boolean enabled) {
		consoleColorsEnabled = enabled;
	}

	//
	// Console colors parsing
	//
	private static String substituteColors(String source) {
		return substituteColors(DEFAULT_THEME, source);
	}

	private static String substituteColors(String[][] theme, String source) {
		return substituteColors(theme, source, true);
	}

	private static String substituteColors(String[][] theme, String source, boolean reset) {
		if (source == null) {
			return "";
		}

		// escape the $ symbol with '/'
		source = escapeColorTag(source);

		Pattern colorTag = Pattern.compile(COLOR_TAG_PATTERN);
		Matcher matcher = colorTag.matcher(source);

		boolean initialMatch = matcher.find();
		int sourceLen = source.length();
		int lastStart = 0;

		String build = "";

		if (initialMatch) {
			do {
				int start = matcher.start();
				int end = matcher.end() + 1;

				String colorType = matcher.group(1);
				String colorValue = matcher.group(2);

				if (colorValue.equals("")) {
					String[] themeTokenData = getThemeToken(theme, colorType);
					colorType = "theme";

					String bgColor = substituteColors(theme, themeTokenData[1], false);
					String textColor = substituteColors(theme, themeTokenData[2], false);

					boolean bgExists = !bgColor.equals("");
					boolean textExists = !textColor.equals("");
					colorValue = bgColor;

					if (bgExists && textExists) {
						colorValue += textColor;
					} else if (textExists) {
						colorValue = textColor;
					}
				}

				switch (colorType) {
					case "bg" -> {
						colorValue = getColor(BG_COLORS, colorValue);
					}
					case "text" -> {
						colorValue = getColor(TEXT_COLORS, colorValue);
					}
				}

				build += source.substring(lastStart, start) + colorValue;
				lastStart = end;

			} while (matcher.find());
		}

		if (lastStart < sourceLen) {
			build += source.substring(lastStart, sourceLen);
		}

		return unescapeColorTag(build)
				+ (reset ? getColor(TEXT_COLORS, "reset") : "");
	}

	private static String escapeColorTag(String str) {
		return str.replaceAll("/\\$", "\0esc");
	}

	private static String unescapeColorTag(String str) {
		return str.replaceAll("\0esc", "\\$");
	}

	private static String replaceColorTags(Object message) {
		String text = "" + message;
		return text.replaceAll(COLOR_TAG_PATTERN + " ", "");
	}

	private static String getColor(String[][] colorList, String name) {
		for (String[] colorData : colorList) {
			if (name.equals(colorData[0])) {
				return colorData[1];
			}
		}

		return "[color not found]";
	}

	private static String[] getThemeToken(String[][] theme, String token) {
		for (String[] themeData : theme) {
			if (token.equals(themeData[0])) {
				return themeData;
			}
		}

		return new String[] { "null", "", "" };
	}
}

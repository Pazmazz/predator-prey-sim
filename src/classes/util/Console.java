/*
 * @Written: 3/29/2025
 * 
 * class Console:
 * 
 * Used for interacting with and styling the console. Much of this code was reused from
 * the final project from the previous semester in CS-190.
 */

package classes.util;

import classes.abstracts.Application;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Console extends Application {
	// Debug info (immutable at runtime)
	final private static Map<DebugPriority, String> debugPrefixes = new HashMap<>() {{
		put(DebugPriority.LOW, "$text-green [Debug Low]$text-reset");
		put(DebugPriority.MEDIUM, "$text-purple [Debug Medium]$text-reset");
		put(DebugPriority.HIGH, "$text-red [Debug High]$text-reset");
		}
	};

	final private static Map<DebugPriority, Boolean> listeningDebugPriorities = new HashMap<>() {{
		put(DebugPriority.LOW, Boolean.TRUE);
		put(DebugPriority.MEDIUM, Boolean.TRUE);
		put(DebugPriority.HIGH, Boolean.TRUE);
	}};

	private static boolean debugModeEnabled = false;
	private static boolean consoleColorsEnabled = false;

	public static enum DebugPriority {
		LOW,
		MEDIUM,
		HIGH
	}
	
	final private static String COLOR_TAG_PATTERN = "\\$([a-zA-Z]+)\\-?([a-zA-Z_]*)";
  final private static String[][] DEFAULT_THEME = 
  {
    {"error", "$bg-black", "$text-white"},
    {"warn", "$bg-blue", "$text-black"}
  };

  final private static String[][] TEXT_COLORS = 
  {
    {"reset", "\u001B[0m"},

    {"black", "\u001B[30m"},
    {"red", "\u001B[31m"},
    {"green", "\u001B[32m"},
    {"yellow", "\u001B[33m"},
    {"blue", "\u001B[34m"},
    {"purple", "\u001B[35m"},
    {"cyan", "\u001B[36m"},
    {"white", "\u001B[37m"},

    {"bright_black", "\u001B[90m"},
    {"bright_red", "\u001B[91m"},
    {"bright_green", "\u001B[92m"},
    {"bright_yellow", "\u001B[93m"},
    {"bright_blue", "\u001B[94m"},
    {"bright_purple", "\u001B[95m"},
    {"bright_cyan", "\u001B[96m"},
    {"bright_white", "\u001B[97m"},
  };

  final private static String[][] BG_COLORS = 
  {
    {"black", "\u001B[40m"},
    {"red", "\u001B[41m"},
    {"green", "\u001B[42m"},
    {"yellow", "\u001B[43m"},
    {"blue", "\u001B[44m"},
    {"purple", "\u001B[45m"},
    {"cyan", "\u001B[46m"},
    {"white", "\u001B[47m"},

    {"bright_black", "\u001B[100m"},
    {"bright_red", "\u001B[101m"},
    {"bright_green", "\u001B[102m"},
    {"bright_yellow", "\u001B[103m"},
    {"bright_blue", "\u001B[104m"},
    {"bright_purple", "\u001B[105m"},
    {"bright_cyan", "\u001B[106m"},
    {"bright_white", "\u001B[107m"}
  };

	public static void println(Object ...contents) {
		if (consoleColorsEnabled)
			System.out.println(substituteColors(Formatter.concatArray(contents)));
		else
			System.out.println(replaceColorTags(Formatter.concatArray(contents)));
	}

	public static void print(String message) {
		if (consoleColorsEnabled)
			System.out.print(substituteColors(message));
		else
			System.out.print(replaceColorTags(message));
	}

	public static void error(Object ...contents) {
		throw new Error(Formatter.concatArray(contents));
	}

	public static boolean isDebugMode() {
		return debugModeEnabled;
	}

	public static void setDebugModeEnabled(boolean enabled) {
		debugModeEnabled = enabled;
	}

	public static void hideDebugPriority(DebugPriority priority) {
		listeningDebugPriorities.put(priority, Boolean.FALSE);
	}

	public static void showDebugPriority(DebugPriority priority) {
		listeningDebugPriorities.put(priority, Boolean.TRUE);
	}

	public static boolean isShowingDebugPriority(DebugPriority priority) {
		return listeningDebugPriorities.get(priority);
	}

	public static void setDebugPriority(DebugPriority priority) {
		for (DebugPriority key : listeningDebugPriorities.keySet()) {
			if (priority == key) {
				listeningDebugPriorities.put(key, Boolean.TRUE);
			} else {
				listeningDebugPriorities.put(key, Boolean.FALSE);
			}
		}
	}

	public static void debugPrint(DebugPriority priority, Object ...messages) {
		if (isDebugMode() && isShowingDebugPriority(priority)) {
			Console.println(
				"%s: %s".formatted(
					debugPrefixes.get(priority), 
					Formatter.concatArray(messages, " | ")
				)
			);
		}
	}

	public static void debugPrint(Object ...messages) {
		debugPrint(DebugPriority.LOW, messages);
	}

	public static void setConsoleColorsEnabled(boolean enabled) {
		consoleColorsEnabled = enabled;
	}

  /*
   * substituteColors() with default: <String[][]> theme, <boolean> reset
   */
  private static String substituteColors(String source) {
    return substituteColors(DEFAULT_THEME, source);
  }

  /*
   * substituteColors() with default: <boolean> reset
   */
  private static String substituteColors(String[][] theme, String source) {
    return substituteColors(theme, source, true);
  }

  /*
   * substituteColors(<String[][]> theme, String source, <boolean> reset):
   * 
   * Takes an input string and parses it for the tokens: "$colorType-colorValue"
   * or "$colorSet". Replaces those tokens with their respective color values inside the
   * TEXT_COLORS and BG_COLORS arrays, and then substitutes them in the newly built 
   * string.
   * 
   */
  private static String substituteColors(String[][] theme, String source, boolean reset) 
  {
    if (source == null) return "";

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

        if (colorValue.equals("")) 
        {
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
          case "bg" ->  { colorValue = getColor(BG_COLORS, colorValue); }
          case "text" ->  { colorValue = getColor(TEXT_COLORS, colorValue); }
        }

        build += source.substring(lastStart, start) + colorValue;
        lastStart = end;

      } while (matcher.find());
    }

    if (lastStart < sourceLen)
      build += source.substring(lastStart, sourceLen);

    return unescapeColorTag(build) 
      + (reset ? getColor(TEXT_COLORS, "reset") : "");
  }

/*
   * escapeColorTag(<String> str):
   * 
   * Escapes the "$colorType-colorValue" tokens in
   * a given string by using the "/" character
   * 
   * Ex:
   *    escapeColorTag("/$text-green hello world") -> "\0esc hello world"
   */
  private static String escapeColorTag(String str) {
    return str.replaceAll("/\\$", "\0esc");
  }

  /*
   * unescapeColorTag(<String> str):
   * 
   * Unescapes an already escaped string and returns the 
   * intended text.
   * 
   * Ex:
   *    unescapeColorTag(escapeColorTag("/$text-green hello world")) -> "$text-green hello world"
   */
  private static String unescapeColorTag(String str) {
    return str.replaceAll("\0esc", "\\$");
  }

  /*
   * replaceColorTags(<Object> message):
   * 
   * Replaces all "$colorType-colorValue" tokens with an
   * empty string - used when the system does not support
   * console colors
   */
  private static String replaceColorTags(Object message) {
    String text = "" + message;
    return text.replaceAll(COLOR_TAG_PATTERN + " ", "");
  }

  /*
   * getColor(<String[][]> colorList, <String> name):
   * 
   * Returns an ASCII console color from a given
   * color list
   */
  private static String getColor(String[][] colorList, String name) 
  {
    for (String[] colorData : colorList)
      if (name.equals(colorData[0])) return colorData[1];

    return "[color not found]";
  }

  /*
   * getThemeToken(<String[][]> theme, String token):
   * 
   * Returns a given theme token from a selected
   * theme array
   */
  private static String[] getThemeToken(String[][] theme, String token) 
  {
    for (String[] themeData : theme)
      if (token.equals(themeData[0])) return themeData;

    return new String[] {"null", "", ""};
  }
}

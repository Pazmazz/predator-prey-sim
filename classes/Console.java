/*
 * @Written: 3/29/2025
 * 
 * class Console:
 * 
 * Used for interacting with and styling the console. Much of this code was reused from
 * the final project from the previous semester in CS-190.
 */

package classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Console {
	// String patterns
	final static String COLOR_TAG_PATTERN = "\\$([a-zA-Z]+)\\-?([a-zA-Z_]*)";

  // Themes
  final static String[][] DEFAULT_THEME = 
  {
    {"error", "$bg-black", "$text-white"},
    {"warn", "$bg-blue", "$text-black"}
  };

  final static String[][] TEXT_COLORS = 
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

  final static String[][] BG_COLORS = 
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
		System.out.println(Formatter.concatArray(contents));
	}

	public static void print(String message) {
		System.out.print(message);
	}

	public static void error(Object ...contents) {
		throw new Error(Formatter.concatArray(contents));
	}

  /*
   * substituteColors() with default: <String[][]> theme, <boolean> reset
   */
  static String substituteColors(String source) {
    return substituteColors(DEFAULT_THEME, source);
  }

  /*
   * substituteColors() with default: <boolean> reset
   */
  static String substituteColors(String[][] theme, String source) {
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
  static String substituteColors(String[][] theme, String source, boolean reset) 
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
        // find the match start/end index
        int start = matcher.start();
        int end = matcher.end() + 1;

        /*
         * get the different captures
         * 
         * ex: $bg-black
         *    capture 1: "bg"
         *    capture 2: "black"
         */
        String colorType = matcher.group(1);
        String colorValue = matcher.group(2);

        /*
         * if no capture 2 (ex: $something):
         *    - capture 1 is treated as a lookup key in `theme`
         * 
         *    - if found, then return the data associated with the key
         *        * data in form: { "key", "bg-color", "text-color" }
         * 
         *    - recursively call method for "bg-color" and "text-color" to retrieve
         *      their values.
         */
        if (colorValue.equals("")) 
        {
          // get theme data in form: { "key", "bg-color", "text-color" }
          String[] themeTokenData = getThemeToken(theme, colorType);

          colorType = "theme";

          // replace "bg-color" and "text-color" with their literal console colors, and combine them
          String bgColor = substituteColors(theme, themeTokenData[1], false);
          String textColor = substituteColors(theme, themeTokenData[2], false);

          // check if either of them is missing in the theme declaration
          boolean bgExists = !bgColor.equals("");
          boolean textExists = !textColor.equals("");

          // bg-color by default
          colorValue = bgColor;

          // if both exist, combine them
          if (bgExists && textExists) {
            colorValue += textColor;

          // if only text-color exists, then re-assign to text-color
          } else if (textExists) {
            colorValue = textColor;
          }
        }

        // switch color list based on color type
        switch (colorType) {
          case "bg": { colorValue = getColor(BG_COLORS, colorValue); break; }
          case "text": { colorValue = getColor(TEXT_COLORS, colorValue); break; }
        }

        // append the string leading up to the match, plus the replaced match value
        build += source.substring(lastStart, start) + colorValue;

        // update the next starting index
        lastStart = end;

      } while (matcher.find());
    }

    // final case, if there is any remaining part of the string after the last match
    if (lastStart < sourceLen)
      build += source.substring(lastStart, sourceLen);

    // return final build and append reset, so colors don't carry over to the next print
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
  static String escapeColorTag(String str) {
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
  static String unescapeColorTag(String str) {
    return str.replaceAll("\0esc", "\\$");
  }

  /*
   * replaceColorTags(<Object> message):
   * 
   * Replaces all "$colorType-colorValue" tokens with an
   * empty string - used when the system does not support
   * console colors
   */
  static String replaceColorTags(Object message) {
    String text = "" + message;
    return text.replaceAll(COLOR_TAG_PATTERN + " ", "");
  }

  /*
   * getColor(<String[][]> colorList, <String> name):
   * 
   * Returns an ASCII console color from a given
   * color list
   */
  static String getColor(String[][] colorList, String name) 
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
  static String[] getThemeToken(String[][] theme, String token) 
  {
    for (String[] themeData : theme)
      if (token.equals(themeData[0])) return themeData;

    return new String[] {"null", "", ""};
  }
}

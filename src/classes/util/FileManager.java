package classes.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

// TODO: Add documentation
public class FileManager {
	public static File getOutputDir() {
		File outputDir = new File("./src/saves");
		if (!outputDir.exists()) {
			outputDir.mkdir();
		}

		return outputDir;
	}

	public static boolean establishFile(File file, boolean required) {
		try {
			getOutputDir();
			if (required && !file.exists()) {
				file.createNewFile();
				System.exit(0);
			}

			boolean fileCreated = file.createNewFile();
			if (fileCreated)
				return true;

			return false;
		} catch (IOException e) {
			Console.println(e.getMessage());
			return false;
		}
	}

	public static void writeFile(File file, String text, boolean append) {
		try {
			establishFile(file, false);
			FileWriter fileWriter = new FileWriter(
					file.getAbsoluteFile(),
					append);

			try (BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
				bufferedWriter.write(text);

			} catch (IOException e) {
				throw new Error("Issue writing to file: " + e.getMessage());
			}
		} catch (IOException e) {
			throw new Error("Issue creating FileWriter: " + e.getMessage());
		}
	}
}

package ivanizki.research.data.file;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.top_logic.basic.StringServices;

import ivanizki.research.DummyLogger;

/**
 * Utilities to work with files.
 *
 * @author ivanizki
 */
public class FileUtil {
	
	/**
	 * @return The {@link String} containing the specified file.
	 */
	public static String readFileToString(String filename) {
		try {
			return StringServices.join(Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8), "");
		} catch (IOException e) {
			DummyLogger.error(e);
		}
		return StringServices.EMPTY_STRING;
	}

	/**
	 * Writes the given {@link String} to the specified file.
	 */
	public static void writeStringToFile(String filename, String string) {
		try (FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8)) {
			writer.write(string);
			writer.close();
		} catch (IOException e) {
			DummyLogger.error(e);
		}
	}

}

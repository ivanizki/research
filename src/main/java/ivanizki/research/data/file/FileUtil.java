package ivanizki.research.data.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.top_logic.basic.StringServices;

import ivanizki.research.DummyLogger;
import ivanizki.research.data.Data;
import ivanizki.research.data.types.Text;
import ivanizki.research.data.types.TextLine;

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
	 * Reads {@link Text} from the specified file.
	 */
	public static Text readText(String filename) {
		Text text = new Text();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
			String line = reader.readLine();
			while (line != null) {
				text.add(new TextLine(line));
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			DummyLogger.error(e);
		}
		return text;
	}

	/**
	 * Writes the given {@link Data} to the specified file.
	 */
	public static void writeToHTML(String filename, Data data) {
		try (FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8)) {
			data.writeToHTML(writer);
			writer.close();
		} catch (IOException e) {
			DummyLogger.error(e);
		}
	}

}

package ivanizki.research.data.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.top_logic.basic.Settings;
import com.top_logic.basic.StringServices;
import com.top_logic.util.error.TopLogicException;

import ivanizki.research.DummyLogger;
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
	 * @return {@link Text} from the specified file.
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
	 * @return All lines from the specified {@link InputStream}.
	 */
	public static List<String> readAllLines(InputStream stream) {
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			DummyLogger.error(e);
		}
		return lines;
	}

	/**
	 * @return A new empty temporary {@link File}.
	 */
	public static File createTempFile(String fileName, String fileExtension) {
		try {
			return File.createTempFile(fileName, fileExtension, Settings.getInstance().getTempDir());
		} catch (IOException ex) {
			throw new TopLogicException(I18NConstants.FAILED_TO_CREATE_FILE, ex);
		}
	}

}

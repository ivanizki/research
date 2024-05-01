package ivanizki.research.data.file;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import ivanizki.research.data.StringUtil;
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
	public static String readFileToString(String filename) throws IOException {
		return StringUtil.join(Files.readAllLines(Paths.get(filename), StandardCharsets.UTF_8), StringUtil.EMPTY);
	}

	/**
	 * @return {@link Text} from the specified file.
	 */
	public static Text readText(String filename) throws IOException {
		Text text = new Text();
		try (BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
			String line = reader.readLine();
			while (line != null) {
				text.add(new TextLine(line));
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			throw e;
		}
		return text;
	}
	
	/**
	 * @return All lines from the specified {@link InputStream}.
	 */
	public static List<String> readAllLines(InputStream stream) throws IOException {
		List<String> lines = new ArrayList<>();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
			String line = reader.readLine();
			while (line != null) {
				lines.add(line);
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			throw e;
		}
		return lines;
	}

}

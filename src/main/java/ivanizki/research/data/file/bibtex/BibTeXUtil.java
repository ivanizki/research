package ivanizki.research.data.file.bibtex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.List;

import ivanizki.research.data.ASCII;
import ivanizki.research.data.StringUtil;
import ivanizki.research.data.file.FileUtil;

/**
 * Utilities to work with {@link BibTeX}.
 *
 * @author ivanizki
 */
public class BibTeXUtil implements BibTeX {

	/**
	 * Writes the given {@link BibTeXDocument} to {@link File}.
	 */
	public static File writeToBibTeX(BibTeXDocument document, File file) throws FileNotFoundException, IOException {
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file))) {
			for (BibTeXEntry entry : document.getEntries()) {
				writer.append(ASCII.AT);
				writer.append(entry.toString());
			}
			writer.close();
			return file;
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * Reads the {@link BibTeXDocument} from the specified {@link InputStream}.
	 */
	public static BibTeXDocument readFromBibTeX(InputStream stream) throws IOException {
		BibTeXDocument document = new BibTeXDocument();
		for (String entry : splitEntries(join(FileUtil.readAllLines(stream)))) {
			document.add(new BibTeXEntry(entry));
		}
		return document;
	}

	private static List<String> splitEntries(String string) {
		return StringUtil.split(string, Character.toString(ASCII.AT));
	}

	private static String join(List<?> values) {
		return StringUtil.join(values, StringUtil.EMPTY);
	}

}

package ivanizki.research.data.file.bibtex;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;

import com.top_logic.basic.StringServices;
import com.top_logic.basic.io.binary.BinaryData;

import ivanizki.research.DummyLogger;
import ivanizki.research.data.ASCII;
import ivanizki.research.data.file.FileUtil;

/**
 * Utilities to work with {@link BibTeX}.
 *
 * @author ivanizki
 */
public class BibTeXUtil implements BibTeX {

	/**
	 * Reads the {@link BibTeXDocument} from the specified {@link BinaryData}.
	 */
	public static BibTeXDocument readFromBibTeX(BinaryData data) {
		try {
			return BibTeXUtil.readFromBibTeX(data.getStream());
		} catch (IOException ex) {
			DummyLogger.error(ex);
		}
		return null;
	}

	/**
	 * Reads the {@link BibTeXDocument} from the specified {@link InputStream}.
	 */
	public static BibTeXDocument readFromBibTeX(InputStream stream) {
		BibTeXDocument document = new BibTeXDocument();
		for (String entry : splitEntries(join(FileUtil.readAllLines(stream)))) {
			document.add(new BibTeXEntry(entry));
		}
		return document;
	}

	private static List<String> splitEntries(String string) {
		return StringServices.toList(string, ASCII.AT);
	}

	private static String join(Collection<?> values) {
		return StringServices.join(values, StringServices.EMPTY_STRING);
	}

}

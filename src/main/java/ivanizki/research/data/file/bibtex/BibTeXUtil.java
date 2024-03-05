package ivanizki.research.data.file.bibtex;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;
import java.util.List;

import com.top_logic.basic.StringServices;
import com.top_logic.basic.io.binary.BinaryData;
import com.top_logic.basic.io.binary.BinaryDataFactory;
import com.top_logic.util.error.TopLogicException;

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
	 * Writes the given {@link BibTeXDocument} to {@link BinaryData}.
	 */
	public static BinaryData writeToBibTeX(BibTeXDocument document) {
		File tempFile = FileUtil.createTempFile("temp-bibtex-file", BibTeX.FILE_EXTENSIONS);
		try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(tempFile))) {
			for (BibTeXEntry entry : document.getEntries()) {
				writer.append(ASCII.AT);
				writer.append(entry.toString());
			}
			writer.close();
			return BinaryDataFactory.createBinaryDataWithName(tempFile, "bibtex-file" + BibTeX.FILE_EXTENSIONS);
		} catch (FileNotFoundException ex) {
			throw new TopLogicException(ivanizki.research.data.file.I18NConstants.FAILED_TO_FIND_FILE, ex);
		} catch (IOException ex) {
			throw new TopLogicException(I18NConstants.FAILED_TO_WRITE_TO_BIBTEX_FILE, ex);
		}
	}

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

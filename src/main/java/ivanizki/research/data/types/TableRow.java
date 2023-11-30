package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import ivanizki.research.data.file.html.HTML;
import ivanizki.research.data.file.html.HTMLUtil;

/**
 * A table row.
 *
 * @author ivanizki
 */
public class TableRow extends Composition<TableCell> {

	/**
	 * Creates a new {@link TableRow}.
	 */
	public TableRow() {
		super();
	}

	/**
	 * @return The {@link TableCell}s of this {@link TableRow}.
	 */
	public List<TableCell> getCells() {
		return getParts();
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		writer.write(HTMLUtil.begin(HTML.TR));
		super.writeToHTML(writer);
		writer.write(HTMLUtil.end(HTML.TR));
	}

	@Override
	public int readFromHTML(Reader reader) throws IOException {
		String begin = HTMLUtil.readBegin(reader);
		if (!begin.contains(Character.toString(HTML.TAG_CLOSURE))) {
			super.readFromHTML(reader);
			String end = HTMLUtil.readEnd(reader);
			return end.charAt(end.length() - 1);
		}
		return begin.charAt(begin.length() - 1);
	}

}

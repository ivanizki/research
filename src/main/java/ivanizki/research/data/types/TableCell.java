package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import ivanizki.research.data.file.html.HTML;
import ivanizki.research.data.file.html.HTMLUtil;

/**
 * A table cell.
 *
 * @author ivanizki
 */
public class TableCell extends DataContainer {

	/**
	 * Creates a new empty {@link TableCell}.
	 */
	public TableCell() {
		super();
	}

	/**
	 * Creates a new {@link TableCell} from the given {@link Data}.
	 */
	public TableCell(Data data) {
		super(data);
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		writer.write(HTMLUtil.begin(HTML.TD));
		super.writeToHTML(writer);
		writer.write(HTMLUtil.end(HTML.TD));
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

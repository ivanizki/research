package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import ivanizki.research.data.file.html.HTML;
import ivanizki.research.data.file.html.HTMLUtil;

/**
 * A table.
 *
 * @author ivanizki
 */
public class Table extends Composition<TableRow> {

	/**
	 * Creates a new {@link Table}.
	 */
	public Table() {
		super();
	}

	/**
	 * @return The {@link TableRow}s of this {@link Table}.
	 */
	public List<TableRow> getRows() {
		return getParts();
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		writer.write(HTMLUtil.begin(HTML.TABLE, HTMLUtil.attribute("border", "1")));
		super.writeToHTML(writer);
		writer.write(HTMLUtil.end(HTML.TABLE));
	}

}

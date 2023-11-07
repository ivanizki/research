package ivanizki.research.data.types;

import java.util.List;

import ivanizki.research.data.file.html.HTML;
import ivanizki.research.data.file.html.HTMLUtil;

/**
 * {@link Composition} representing a table.
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
	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		sb.append(HTMLUtil.begin(HTML.TABLE, HTMLUtil.attribute("border", "1")));
		for (TableRow row : getRows()) {
			sb.append(row.toHTML());
		}
		sb.append(HTMLUtil.end(HTML.TABLE));
		return sb.toString();
	}

}

package ivanizki.research.data.types;

import java.util.List;

import ivanizki.research.data.file.html.HTML;
import ivanizki.research.data.file.html.HTMLUtil;

/**
 * {@link Composition} representing a row of a {@link Table}.
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
	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		sb.append(HTMLUtil.begin(HTML.TR));
		for (TableCell cell : getCells()) {
			sb.append(cell.toHTML());
		}
		sb.append(HTMLUtil.end(HTML.TR));
		return sb.toString();
	}

}

package ivanizki.research.data.types;

import ivanizki.research.data.file.html.HTML;
import ivanizki.research.data.file.html.HTMLCompatible;
import ivanizki.research.data.file.html.HTMLUtil;

/**
 * A {@link HTMLCompatible} table cell.
 *
 * @author ivanizki
 */
public class TableCell implements HTMLCompatible {

	private HTMLCompatible _cell;

	/**
	 * Creates a new {@link TableCell} from the given {@link HTMLCompatible}.
	 */
	public TableCell(HTMLCompatible cell) {
		_cell = cell;
	}

	@Override
	public String toHTML() {
		return new StringBuilder()
			.append(HTMLUtil.begin(HTML.TD))
			.append(_cell.toHTML())
			.append(HTMLUtil.end(HTML.TD))
			.toString();
	}
}

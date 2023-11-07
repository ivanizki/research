package ivanizki.research.data.types;

import ivanizki.research.data.file.html.HTMLCompatible;

/**
 * A {@link HTMLCompatible} {@link String}.
 *
 * @author ivanizki
 */
public class Text implements HTMLCompatible {

	private String _string;

	/**
	 * Creates a new {@link Text} from the given {@link String}.
	 */
	public Text(String string) {
		_string = string;
	}

	@Override
	public String toHTML() {
		return _string;
	}
}

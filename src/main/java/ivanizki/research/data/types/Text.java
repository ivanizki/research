package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Writer;

import ivanizki.research.data.Data;

/**
 * A text.
 *
 * @author ivanizki
 */
public class Text implements Data {

	private String _string;

	/**
	 * Creates a new {@link Text} from the given {@link String}.
	 */
	public Text(String string) {
		_string = string;
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		writer.write(_string);
	}

}

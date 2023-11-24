package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Writer;

import ivanizki.research.data.Data;

/**
 * A text line.
 *
 * @author ivanizki
 */
public class TextLine implements Data {

	private String _string;

	/**
	 * Creates a new {@link TextLine} from the given {@link String}.
	 */
	public TextLine(String string) {
		_string = string;
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		writer.write(_string);
	}

}

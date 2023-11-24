package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Writer;

/**
 * A text line.
 *
 * @author ivanizki
 */
public class TextLine extends Container<String> {

	/**
	 * Creates a new {@link TextLine} from the given {@link String}.
	 */
	public TextLine(String string) {
		super();
		setContent(string);
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		writer.write(getContent());
	}

}

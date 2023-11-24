package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import ivanizki.research.data.ASCII;

/**
 * A text.
 *
 * @author ivanizki
 */
public class Text extends Composition<TextLine> {

	/**
	 * Creates a new {@link Text}.
	 */
	public Text() {
		super();
	}

	/**
	 * @return The {@link TextLine}s of this {@link Text}.
	 */
	public List<TextLine> getLines() {
		return getParts();
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		super.writeToHTML(writer);
		for (TextLine line : getLines()) {
			line.writeToHTML(writer);
			writer.write(ASCII.NEWLINE);
		}
	}

}

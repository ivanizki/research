package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import ivanizki.research.data.Container;
import ivanizki.research.data.file.html.HTMLUtil;

/**
 * A text line.
 *
 * @author ivanizki
 */
public class TextLine extends Container<String> implements Data {

	/**
	 * Creates a new {@link TextLine} from the given {@link String}.
	 */
	public TextLine(String string) {
		super();
		setContent(string);
	}

	/**
	 * @return The {@link String} of this {@link TextLine}.
	 */
	public String getString() {
		return getContent();
	}

	/**
	 * Sets the {@link String} of this {@link TextLine}.
	 */
	public void setString(String uid) {
		setContent(uid);
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		writer.write(getContent());
	}

	@Override
	public int readFromHTML(Reader reader) throws IOException {
		DataPointer linePointer = new DataPointer();
		int c = HTMLUtil.readPlainData(reader, linePointer);
		setString(((TextLine) linePointer.getObject()).getString());
		return c;
	}

}

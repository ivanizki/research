package ivanizki.research.data.file.html;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Interface for {@link Object}s compatible with {@link HTML}.
 *
 * @author ivanizki
 */
public interface HTMLCompatible {

	/**
	 * Writes an {@link HTML}-representation of this {@link Object} via the given {@link Writer}.
	 */
	public void writeToHTML(Writer writer) throws IOException;

	/**
	 * Reads an {@link HTML}-representation of this {@link Object} via the given {@link Reader}.
	 * 
	 * @return The last character read, or -1, if the {@link Reader} has finished.
	 */
	public int readFromHTML(Reader reader) throws IOException;
}

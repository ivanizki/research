package ivanizki.research.data.file.html;

import java.io.IOException;
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
}

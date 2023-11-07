package ivanizki.research.data;

import java.util.List;

/**
 * Interface for composite {@link Object}s.
 *
 * @author ivanizki
 */
public interface Composite<T> {

	/**
	 * @return The list of composing parts.
	 */
	public List<T> getParts();
}

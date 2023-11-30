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

	/**
	 * @return Whether this {@link Composite} has no {@link #getParts() parts}.
	 */
	public default boolean isEmpty() {
		return getParts().isEmpty();
	}

	/**
	 * @return Number of {@link #getParts() parts} in this {@link Composite}.
	 */
	public default int size() {
		return getParts().size();
	}

}

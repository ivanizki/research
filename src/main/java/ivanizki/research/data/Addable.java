package ivanizki.research.data;

/**
 * Interface for addable {@link Object}s.
 *
 * @author ivanizki
 */
public interface Addable<T> {

	/**
	 * Adds the given {@link Object}.
	 * 
	 * @return Whether the addition was successful.
	 */
	public boolean add(T addendum);
}

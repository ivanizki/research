package ivanizki.research.data;

/**
 * A pointer to an {@link Object}.
 *
 * @author ivanizki
 */
public class Pointer<T> extends Container<T> {

	/**
	 * Creates a new empty {@link Pointer}.
	 */
	public Pointer() {
		setObject(null);
	}

	/**
	 * Creates a new {@link Pointer}.
	 */
	public Pointer(T object) {
		setObject(object);
	}

	/**
	 * @return The object of this {@link Pointer}.
	 */
	public T getObject() {
		return getContent();
	}

	/**
	 * Sets the object of this {@link Pointer}.
	 */
	public void setObject(T object) {
		setContent(object);
	}

	/**
	 * @return Whether this {@link Pointer} points to <code>null</code>.
	 */
	public boolean isNullPointer() {
		return getObject() == null;
	}

}

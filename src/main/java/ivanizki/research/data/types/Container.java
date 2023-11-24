package ivanizki.research.data.types;

/**
 * A container.
 *
 * @author ivanizki
 */
public abstract class Container<T> implements Data {

	T _content;

	/**
	 * @return The content of this {@link Container}.
	 */
	public T getContent() {
		return _content;
	}

	/**
	 * Sets the content of this {@link Container}.
	 */
	protected void setContent(T content) {
		_content = content;
	}
}

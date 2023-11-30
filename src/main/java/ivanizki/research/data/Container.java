package ivanizki.research.data;

/**
 * A container.
 *
 * @author ivanizki
 */
public abstract class Container<T> {

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

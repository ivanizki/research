package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import ivanizki.research.data.Addable;
import ivanizki.research.data.Composite;

/**
 * A composition.
 *
 * @author ivanizki
 */
public class Composition<T extends Data> extends Container<List<T>> implements Addable<T>, Composite<T> {

	/**
	 * Creates a new {@link Composition}.
	 */
	public Composition() {
		setContent(new ArrayList<>());
	}

	@Override
	public List<T> getParts() {
		return getContent();
	}

	@Override
	public boolean add(T addendum) {
		return getParts().add(addendum);
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		for (T part : getParts()) {
			part.writeToHTML(writer);
		}
	}

}

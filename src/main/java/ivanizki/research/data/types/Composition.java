package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import ivanizki.research.data.Addable;
import ivanizki.research.data.Composite;
import ivanizki.research.data.Data;

/**
 * A composition.
 *
 * @author ivanizki
 */
public class Composition<T extends Data> implements Addable<T>, Composite<T>, Data {

	private ArrayList<T> _parts;

	/**
	 * Creates a new {@link Composition}.
	 */
	public Composition() {
		_parts = new ArrayList<>();
	}

	@Override
	public List<T> getParts() {
		return _parts;
	}

	@Override
	public boolean add(T addendum) {
		return _parts.add(addendum);
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		for (T part : _parts) {
			part.writeToHTML(writer);
		}
	}

}

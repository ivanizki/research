package ivanizki.research.data.types;

import java.util.ArrayList;
import java.util.List;

import ivanizki.research.data.Addable;
import ivanizki.research.data.Composite;
import ivanizki.research.data.file.html.HTMLCompatible;

/**
 * A {@link HTMLCompatible} composition.
 *
 * @author ivanizki
 */
public class Composition<T extends HTMLCompatible> implements Addable<T>, Composite<T>, HTMLCompatible {

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
	public String toHTML() {
		StringBuilder sb = new StringBuilder();
		for (T part : _parts) {
			sb.append(part.toHTML());
		}
		return sb.toString();
	}

}

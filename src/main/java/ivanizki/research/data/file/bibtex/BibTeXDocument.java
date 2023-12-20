package ivanizki.research.data.file.bibtex;

import java.util.ArrayList;
import java.util.List;

import ivanizki.research.data.Addable;
import ivanizki.research.data.Composite;
import ivanizki.research.data.Container;

/**
 * {@link BibTeX} document.
 *
 * @author ivanizki
 */
public class BibTeXDocument extends Container<List<BibTeXEntry>> implements Addable<BibTeXEntry>, Composite<BibTeXEntry> {

	/**
	 * Creates a new {@link BibTeXDocument}.
	 */
	public BibTeXDocument() {
		setContent(new ArrayList<>());
	}

	/**
	 * @return The {@link BibTeXEntry}s of this {@link BibTeXDocument}.
	 */
	public List<BibTeXEntry> getEntries() {
		return getParts();
	}

	@Override
	public List<BibTeXEntry> getParts() {
		return getContent();
	}

	@Override
	public boolean add(BibTeXEntry addendum) {
		return getParts().add(addendum);
	}

}

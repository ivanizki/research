package ivanizki.research.data.file.bibtex;

/**
 * Interface to work with BibTeX.
 *
 * @author ivanizki
 */
public interface BibTeX {

	/** {@link BibTeX} file extensions. */
	public static final String FILE_EXTENSIONS = ".bib";

	/**
	 * {@link BibTeXEntry} type.
	 */
	@SuppressWarnings("javadoc")
	public enum BibTeXEntryType {
		ARTICLE, BOOK, PHDTHESIS
	}

	/**
	 * {@link BibTeXEntry} attribute.
	 */
	@SuppressWarnings("javadoc")
	public enum BibTeXEntryAttribute {
		// General
		TITLE, YEAR, AUTHOR,
		// {@link BibTeXEntryType#ARTICLE}
		JOURNAL, VOLUME, NUMBER, PAGES,
		// {@link BibTeXEntryType#BOOK}
		PUBLISHER, ADDRESS, EDITION,
		// {@link BibTeXEntryType#PHDTHESIS}
		SCHOOL
	}

}

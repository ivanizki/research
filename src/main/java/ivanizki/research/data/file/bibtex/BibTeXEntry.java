package ivanizki.research.data.file.bibtex;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import ivanizki.research.data.ASCII;
import ivanizki.research.data.file.bibtex.BibTeX.BibTeXEntryAttribute;
import ivanizki.research.data.file.bibtex.BibTeX.BibTeXEntryType;

/**
 * {@link BibTeX} entry.
 *
 * @author ivanizki
 */
public class BibTeXEntry {

	private BibTeXEntryType _type;

	private String _id;

	private Map<BibTeXEntryAttribute, String> _attributes = new HashMap<>();

	/**
	 * Creates a new {@link BibTeXEntry} from the given {@link String} representation.
	 */
	public BibTeXEntry(String string) {
		parse(string);
	}

	private void parse(String entry) {
		entry = entry.trim();
		if (!entry.isEmpty()) {
			// Parse the type of the BibTeX entry.
			int beginIndex = 0;
			int endIndex = entry.indexOf(ASCII.PAR_BEGIN, beginIndex);
			_type = BibTeXEntryType.valueOf(entry.substring(beginIndex, endIndex).toUpperCase());

			// Parse the id of the BibTeX entry.
			beginIndex = endIndex + 1;
			endIndex = entry.indexOf(ASCII.COMMA, beginIndex);
			_id = entry.substring(beginIndex, endIndex);

			// Parse the attribute pairs (name="value").
			beginIndex = endIndex + 1;
			endIndex = entry.lastIndexOf(ASCII.QUOTE) + 1;
			entry = entry.substring(beginIndex, endIndex).trim();
			while (!entry.isEmpty()) {
				endIndex = entry.indexOf(ASCII.EQUAL);
				String attributeName = entry.substring(0, endIndex).trim();
				beginIndex = entry.indexOf(ASCII.QUOTE, endIndex + 1) + 1;
				endIndex = entry.indexOf(ASCII.QUOTE, beginIndex);
				String attributeValue = entry.substring(beginIndex, endIndex).trim();
				entry = entry.substring(endIndex + 1).trim();
				if (!entry.isEmpty() && entry.charAt(0) == ASCII.COMMA) {
					entry = entry.substring(1).trim();
				}
				BibTeXEntryAttribute attribute = BibTeXEntryAttribute.valueOf(attributeName.toUpperCase());
				_attributes.put(attribute, attributeValue);
			}
		}
	}

	/**
	 * @return The {@link BibTeXEntryType type} of this {@link BibTeXEntry}.
	 */
	public BibTeXEntryType getType() {
		return _type;
	}

	/**
	 * @return The id of this {@link BibTeXEntry}.
	 */
	public String getId() {
		return _id;
	}

	/**
	 * @return The {@link BibTeXEntryAttribute}s of this {@link BibTeXEntry}.
	 */
	public Set<BibTeXEntryAttribute> getAttributes() {
		return _attributes.keySet();
	}

	/**
	 * @return The value of the given {@link BibTeXEntryAttribute}.
	 */
	public String getAttributeValue(BibTeXEntryAttribute attribute) {
		return _attributes.get(attribute);
	}

}

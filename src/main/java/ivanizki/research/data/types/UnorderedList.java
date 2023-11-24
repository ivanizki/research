package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import ivanizki.research.data.file.html.HTML;
import ivanizki.research.data.file.html.HTMLUtil;

/**
 * An unordered list.
 *
 * @author ivanizki
 */
public class UnorderedList extends Composition<ListItem> {

	/**
	 * Creates a new {@link UnorderedList}.
	 */
	public UnorderedList() {
		super();
	}

	/**
	 * @return The {@link ListItem}s of this {@link UnorderedList}.
	 */
	public List<ListItem> getItems() {
		return getParts();
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		writer.write(HTMLUtil.begin(HTML.UL));
		super.writeToHTML(writer);
		writer.write(HTMLUtil.end(HTML.UL));
	}
}

package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Writer;

import ivanizki.research.data.file.html.HTML;
import ivanizki.research.data.file.html.HTMLUtil;

/**
 * A list item.
 *
 * @author ivanizki
 */
public class ListItem extends DataContainer {

	/**
	 * Creates a new {@link ListItem} from the given {@link Data}.
	 */
	public ListItem(Data data) {
		super(data);
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		writer.write(HTMLUtil.begin(HTML.LI));
		super.writeToHTML(writer);
		writer.write(HTMLUtil.end(HTML.LI));
	}
}

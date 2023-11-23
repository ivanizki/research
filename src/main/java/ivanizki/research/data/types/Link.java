package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Writer;

import com.top_logic.basic.StringServices;

import ivanizki.research.data.ASCII;
import ivanizki.research.data.Data;
import ivanizki.research.data.file.html.HTML;
import ivanizki.research.data.file.html.HTMLUtil;

/**
 * A link.
 * 
 * <p>
 * This link is loose, that is, a link definition does not require a reference to it.
 * </p>
 *
 * @author ivanizki
 */
public class Link implements Data {

	private String _uid;

	private String _label;

	/**
	 * Creates a new {@link Link}.
	 * 
	 * @param uid
	 *        The unique ID of this {@link Link}.
	 */
	public Link(String uid) {
		this(uid, null);
	}

	/**
	 * Creates a new {@link Link}.
	 * 
	 * @param uid
	 *        The unique ID of this {@link Link}.
	 * @param label
	 *        The {@link String} representation of this {@link Link}.
	 */
	public Link(String uid, String label) {
		_uid = uid;
		_label = label;
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		String uid = StringServices.replace(_uid, ASCII.COLON, ASCII.UNDERSCORE);
		if (_label == null) {
			writer.write(HTMLUtil.tag(HTML.A, HTMLUtil.attribute(HTML.NAME, uid)));
		} else {
			writer.write(HTMLUtil.begin(HTML.A, HTMLUtil.attribute(HTML.HREF, ASCII.SHARP + uid)));
			writer.write(_label);
			writer.write(HTMLUtil.end(HTML.A));
		}
	}
}

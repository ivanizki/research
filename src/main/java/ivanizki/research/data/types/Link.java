package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import com.top_logic.basic.StringServices;

import ivanizki.research.data.ASCII;
import ivanizki.research.data.Container;
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
public class Link extends Container<String> implements Data {

	private String _label;

	/**
	 * Creates a new empty {@link Link}.
	 */
	public Link() {
		super();
	}

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
		super();
		setContent(uid);
		_label = label;
	}

	/**
	 * @return The UID of this {@link Link}.
	 */
	public String getUid() {
		return getContent();
	}

	/**
	 * Sets the UID of this {@link Link}.
	 */
	public void setUid(String uid) {
		setContent(uid);
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		String uid = StringServices.replace(getUid(), ASCII.COLON, Character.toString(ASCII.UNDERSCORE));
		if (_label == null) {
			writer.write(HTMLUtil.tag(HTML.A, HTMLUtil.attribute(HTML.NAME, uid)));
		} else {
			writer.write(HTMLUtil.begin(HTML.A, HTMLUtil.attribute(HTML.HREF, new StringBuilder().append(ASCII.SHARP).append(uid).toString())));
			writer.write(_label);
			writer.write(HTMLUtil.end(HTML.A));
		}
	}

	@Override
	public int readFromHTML(Reader reader) throws IOException {
		Map<String, String> attributes = new HashMap<>();
		String begin = HTMLUtil.readBegin(reader, attributes);
		setUid(attributes.get(HTML.NAME) == null ? attributes.get(HTML.HREF).substring(1) : attributes.get(HTML.NAME));
		if (!begin.contains(Character.toString(HTML.TAG_CLOSURE))) {
			DataPointer labelPointer = new DataPointer();
			HTMLUtil.readPlainData(reader, labelPointer);
			_label = ((TextLine) labelPointer.getObject()).getString();
			String end = HTMLUtil.readEnd(reader);
			return end.charAt(end.length() - 1);
		}
		return begin.charAt(begin.length() - 1);
	}
}

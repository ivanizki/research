package ivanizki.research.layout.manuscript;

import java.io.IOException;

import com.top_logic.basic.StringServices;
import com.top_logic.basic.xml.TagWriter;
import com.top_logic.layout.DisplayContext;
import com.top_logic.layout.Renderer;
import com.top_logic.mig.html.HTMLConstants;

/**
 * {@link Renderer} that renders DOI as a HTML link.
 *
 * @author ivanizki
 */
public class DoiLinkRenderer implements Renderer<Object> {

	/** Singleton instance. */
	public static final DoiLinkRenderer INSTANCE = new DoiLinkRenderer();

	@Override
	public void write(DisplayContext context, TagWriter out, Object value) throws IOException {
		out.beginBeginTag(HTMLConstants.ANCHOR);
		String link = StringServices.toString(value);
		out.writeAttribute(HTMLConstants.HREF_ATTR, link);
		out.writeAttribute(HTMLConstants.TARGET_ATTR, HTMLConstants.BLANK_VALUE);
		out.endBeginTag();
		out.writeText(link);
		out.endTag(HTMLConstants.ANCHOR);
	}

}

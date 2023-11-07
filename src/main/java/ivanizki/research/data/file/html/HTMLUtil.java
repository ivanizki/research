package ivanizki.research.data.file.html;

import ivanizki.research.data.ASCII;

/**
 * Utilities to work with {@link HTML}.
 *
 * @author ivanizki
 */
public class HTMLUtil implements HTML {

	/**
	 * @return The beginning tag with the given name.
	 */
	public static String begin(String tagName, String... attributes) {
		StringBuilder sb = new StringBuilder();
		sb.append(BEGIN_TAG);
		sb.append(tagName);
		for (String attribute : attributes) {
			sb.append(attribute);
		}
		sb.append(END_TAG);
		return sb.toString();
	}

	/**
	 * @return The ending tag with the given name.
	 */
	public static String end(String tagName) {
		return BEGIN_TAG + TAG_CLOSURE + tagName + END_TAG;
	}
	
	/**
	 * @return The closed tag with the given name.
	 */
	public static String tag(String tagName, String... attributes) {
		StringBuilder sb = new StringBuilder();
		sb.append(BEGIN_TAG);
		sb.append(tagName);
		for (String attribute : attributes) {
			sb.append(attribute);
		}
		sb.append(TAG_CLOSURE);
		sb.append(END_TAG);
		return sb.toString();
	}

	/**
	 * @return The tag attribute pair.
	 */
	public static String attribute(String name, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append(ASCII.SPACE);
		sb.append(name);
		sb.append(ASCII.EQUAL);
		sb.append(ASCII.QUOTE);
		sb.append(value);
		sb.append(ASCII.QUOTE);
		return sb.toString();
	}

}

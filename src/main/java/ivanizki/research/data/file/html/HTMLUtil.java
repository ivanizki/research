package ivanizki.research.data.file.html;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import ivanizki.research.data.ASCII;
import ivanizki.research.data.types.Composition;
import ivanizki.research.data.types.Data;
import ivanizki.research.data.types.DataPointer;
import ivanizki.research.data.types.Link;
import ivanizki.research.data.types.ListItem;
import ivanizki.research.data.types.Table;
import ivanizki.research.data.types.TableCell;
import ivanizki.research.data.types.TableRow;
import ivanizki.research.data.types.TextLine;
import ivanizki.research.data.types.UnorderedList;

/**
 * Utilities to work with {@link HTML}.
 *
 * @author ivanizki
 */
public class HTMLUtil implements HTML {

	private static final Map<String, String> TRANSFORMATIONS = initTransformations();

	private static final String END_OF_FILE = "End of file.";

	private static final String FAILED_TO_READ_TAG_BEGIN = "Failed to read tag begin.";

	private static final String FAILED_TO_READ_TAG_END = "Failed to read tag end.";

	private static final String FAILED_TO_READ_TAG_NAME = "Failed to read tag name.";

	private static final String UNKNOWN_TAG = "Unknown tag";

	private static Map<String, String> initTransformations() {
		Map<String, String> map = new HashMap<>();
		map.put(Character.toString(HTML.BEGIN_TAG), HTML.CODE_LT);
		map.put(Character.toString(HTML.END_TAG), HTML.CODE_GT);
		return map;
	}

	/**
	 * @return The beginning tag with the given name.
	 */
	public static String begin(String tagName, String... attributes) {
		StringBuilder sb = new StringBuilder();
		sb.append(ASCII.NEWLINE);
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
		return new StringBuilder()
			.append(ASCII.NEWLINE)
			.append(BEGIN_TAG)
			.append(TAG_CLOSURE)
			.append(tagName)
			.append(END_TAG)
			.toString();
	}
	
	/**
	 * @return The closed tag with the given name.
	 */
	public static String tag(String tagName, String... attributes) {
		StringBuilder sb = new StringBuilder();
		sb.append(ASCII.NEWLINE);
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

	/**
	 * Writes the given {@link Data} to the specified file.
	 */
	public static void writeToHTML(String filename, Data data) throws IOException {
		try (FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8)) {
			data.writeToHTML(writer);
			writer.close();
		} catch (IOException e) {
			throw e;
		}
	}

	/**
	 * Reads all {@link Data} from the specified file.
	 */
	public static Data readFromHTML(String filename) throws IOException {
		Composition<Data> data = new Composition<>();
		try (FileReader reader = new FileReader(filename, StandardCharsets.UTF_8)) {
			data.readFromHTML(reader);
			reader.close();
		} catch (IOException e) {
			throw e;
		}
		return Composition.getSimplifiedData(data);
	}

	/**
	 * Reads the {@link TextLine} from the plain text.
	 */
	public static int readPlainData(Reader reader, DataPointer dataPointer) throws IOException {
		StringBuilder plainContent = new StringBuilder();
		Data data = dataPointer.getObject();
		if (data instanceof TextLine) {
			plainContent.append(((TextLine) data).getString());
		}
		int c = reader.read();
		while (c > -1 && c != BEGIN_TAG) {
			plainContent.append((char) c);
			c = reader.read();
		}
		String string = plainContent.toString();
		dataPointer.setObject(isEmpty(string) ? null : new TextLine(HTMLUtil.transformFromHTML(string.trim())));
		return c;
	}

	/**
	 * @return Whether the given {@link String} is empty in the sense of {@link HTML}.
	 */
	public static boolean isEmpty(String string) {
		for (char c : string.toCharArray()) {
			if (c > ASCII.SPACE) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Reads the {@link Data} from the tag.
	 */
	public static int readTagData(Reader reader, DataPointer dataPointer) throws IOException {
		StringBuilder tagName = new StringBuilder();
		int c = reader.read();
		while (c > -1) {
			tagName.append((char) c);
			if (c >= '0' && c <= '9' || c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z') {
				Data data = toData(tagName.toString());
				dataPointer.setObject(data);
				if (data == null) {
					c = reader.read();
				} else {
					return data.readFromHTML(reader);
				}
			} else if (c == TAG_CLOSURE) {
				return c;
			} else {
				throw new IOException(new StringBuilder()
					.append(FAILED_TO_READ_TAG_NAME).append(ASCII.SPACE)
					.append(UNKNOWN_TAG)
					.append(ASCII.QUOTE)
					.append(tagName.toString())
					.append(ASCII.QUOTE)
					.append(ASCII.DOT)
					.toString());
			}
		}
		throw new IOException(new StringBuilder()
			.append(FAILED_TO_READ_TAG_NAME)
			.append(ASCII.SPACE)
			.append(END_OF_FILE)
			.toString());
	}

	private static Data toData(String tagName) {
		switch (tagName) {
			case A:
				return new Link();
			case LI:
				return new ListItem();
			case TABLE:
				return new Table();
			case TD:
				return new TableCell();
			case TR:
				return new TableRow();
			case UL:
				return new UnorderedList();
		}
		return null;
	}

	/**
	 * @return The {@link String} containing the {@link #begin} and the {@link Map} containing
	 *         {@link #attribute}s.
	 */
	public static String readBegin(Reader reader, Map<String, String> attributes) throws IOException {
		String begin = readBegin(reader);
		readAttributes(begin, attributes);
		return begin;
	}

	/**
	 * @return The {@link String} containing the {@link #begin}.
	 */
	public static String readBegin(Reader reader) throws IOException {
		return readUntilEndTag(reader, FAILED_TO_READ_TAG_BEGIN);
	}

	/**
	 * @return The {@link String} containing the {@link #end}.
	 */
	public static String readEnd(Reader reader) throws IOException {
		return readUntilEndTag(reader, FAILED_TO_READ_TAG_END);
	}

	/**
	 * @return The {@link String} from the given {@link Reader} until the {@link #END_TAG} is
	 *         reached.
	 */
	public static String readUntilEndTag(Reader reader, String eofMessagePrefix) throws IOException {
		StringBuilder begin = new StringBuilder();
		int c = reader.read();
		while (c > -1 && c != END_TAG) {
			begin.append((char) c);
			c = reader.read();
		}
		if (c == END_TAG) {
			begin.append(END_TAG);
			return begin.toString();
		}
		throw new IOException(new StringBuilder()
			.append(eofMessagePrefix)
			.append(ASCII.SPACE)
			.append(END_OF_FILE)
			.toString());
	}

	private static void readAttributes(String begin, Map<String, String> attributes) {
		String string = begin;
		while (string.contains(Character.toString(ASCII.EQUAL))) {
			int equal = string.indexOf(ASCII.EQUAL);
			String attributeName = string.substring(0, equal).trim();
			int firstQuote = string.indexOf(ASCII.QUOTE);
			int secondQuote = string.indexOf(ASCII.QUOTE, firstQuote + 1);
			String attributeValue = string.substring(firstQuote + 1, secondQuote);
			string = string.substring(secondQuote + 1);
			attributes.put(attributeName, attributeValue);
		}
	}

	/**
	 * @return {@link HTML}-representation transformed from the given {@link String}.
	 */
	public static String transformToHTML(String string) {
		String html = string;
		for (Entry<String, String> transformation : TRANSFORMATIONS.entrySet()) {
			html = html.replace(transformation.getKey(), transformation.getValue());
		}
		return html;
	}

	/**
	 * @return {@link String} transformed from the given {@link HTML}-representation.
	 */
	public static String transformFromHTML(String html) {
		String string = html;
		for (Entry<String, String> transformation : TRANSFORMATIONS.entrySet()) {
			string = string.replace(transformation.getValue(), transformation.getKey());
		}
		return string;
	}
}

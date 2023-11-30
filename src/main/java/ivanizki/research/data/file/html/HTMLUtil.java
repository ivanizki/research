package ivanizki.research.data.file.html;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import ivanizki.research.DummyLogger;
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

	private static final String END_OF_FILE = "End of file.";

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
		return ASCII.NEWLINE + BEGIN_TAG + TAG_CLOSURE + tagName + END_TAG;
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
	public static void writeToHTML(String filename, Data data) {
		try (FileWriter writer = new FileWriter(filename, StandardCharsets.UTF_8)) {
			data.writeToHTML(writer);
			writer.close();
		} catch (IOException e) {
			DummyLogger.error(e);
		}
	}

	/**
	 * Reads all {@link Data} from the specified file.
	 */
	public static Data readFromHTML(String filename) {
		Composition<Data> data = new Composition<>();
		try (FileReader reader = new FileReader(filename, StandardCharsets.UTF_8)) {
			data.readFromHTML(reader);
			reader.close();
		} catch (IOException e) {
			DummyLogger.error(e);
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
		dataPointer.setObject(isEmpty(string) ? null : new TextLine(string.trim()));
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
				throw new IOException("Failed to read tag name: Unknown tag \"" + tagName.toString() + "\".");
			}
		}
		throw new IOException("Failed to read tag name." + " " + END_OF_FILE);
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
		return readUntilEndTag(reader, "Failed to read tag begin.");
	}

	/**
	 * @return The {@link String} containing the {@link #end}.
	 */
	public static String readEnd(Reader reader) throws IOException {
		return readUntilEndTag(reader, "Failed to read tag end.");
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
		throw new IOException(eofMessagePrefix + " " + END_OF_FILE);
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

}

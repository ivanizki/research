package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import ivanizki.research.data.Addable;
import ivanizki.research.data.Composite;
import ivanizki.research.data.Container;
import ivanizki.research.data.file.html.HTMLUtil;

/**
 * A composition.
 *
 * @author ivanizki
 */
public class Composition<T extends Data> extends Container<List<T>> implements Addable<T>, Composite<T>, Data {

	/**
	 * Creates a new {@link Composition}.
	 */
	public Composition() {
		setContent(new ArrayList<>());
	}

	@Override
	public List<T> getParts() {
		return getContent();
	}

	@Override
	public boolean add(T addendum) {
		return getParts().add(addendum);
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		for (T part : getParts()) {
			part.writeToHTML(writer);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public int readFromHTML(Reader reader) throws IOException {
		int c = reader.read();
		while (c > -1) {
			DataPointer dataPointer = new DataPointer();
			if (c == HTMLUtil.TAG_CLOSURE) {
				return c;
			} else if (c == HTMLUtil.BEGIN_TAG) {
				c = HTMLUtil.readTagData(reader, dataPointer);
			} else {
				if (c != HTMLUtil.END_TAG) {
					dataPointer.setObject(new TextLine(Character.toString(c)));
				}
				c = HTMLUtil.readPlainData(reader, dataPointer);
			}
			if (!dataPointer.isNullPointer()) {
				add((T) dataPointer.getObject());
			}
		}
		return c;
	}

	/**
	 * @return The simplified {@link Data} from the given {@link Composition}.
	 */
	public static <T extends Data> Data getSimplifiedData(Composition<T> composition) {
		if (composition.isEmpty()) {
			return null;
		}
		if (composition.size() == 1) {
			return composition.getParts().get(0);
		}
		return composition;
	}

}

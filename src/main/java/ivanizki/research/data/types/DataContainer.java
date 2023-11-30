package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import ivanizki.research.data.Container;

/**
 * A {@link Container} of {@link Data}.
 *
 * @author ivanizki
 */
public class DataContainer extends Container<Data> implements Data {

	/**
	 * Creates a new {@link DataContainer} from the given {@link Data}.
	 */
	public DataContainer(Data data) {
		this();
		setContent(data);
	}

	/**
	 * Creates a new empty {@link DataContainer}.
	 */
	public DataContainer() {
		super();
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		getContent().writeToHTML(writer);
	}

	@Override
	public int readFromHTML(Reader reader) throws IOException {
		Composition<Data> data = new Composition<>();
		int c = data.readFromHTML(reader);
		setContent(Composition.getSimplifiedData(data));
		return c;
	}
}

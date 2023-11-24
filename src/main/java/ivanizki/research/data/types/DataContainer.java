package ivanizki.research.data.types;

import java.io.IOException;
import java.io.Writer;

/**
 * A {@link Container} of {@link Data}.
 *
 * @author ivanizki
 */
public class DataContainer extends Container<Data> {

	/**
	 * Creates a new {@link DataContainer} from the given {@link Data}.
	 */
	public DataContainer(Data data) {
		super();
		setContent(data);
	}

	@Override
	public void writeToHTML(Writer writer) throws IOException {
		getContent().writeToHTML(writer);
	}
}

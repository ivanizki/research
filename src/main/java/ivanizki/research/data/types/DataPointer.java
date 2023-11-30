package ivanizki.research.data.types;

import ivanizki.research.data.Pointer;

/**
 * A {@link Pointer} to {@link Data}.
 *
 * @author ivanizki
 */
public class DataPointer extends Pointer<Data> {

	/**
	 * Creates a new empty {@link DataPointer}.
	 */
	public DataPointer() {
		super();
	}

	/**
	 * Creates a new {@link DataPointer}.
	 */
	public DataPointer(Data data) {
		super(data);
	}

}

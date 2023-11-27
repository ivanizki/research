package ivanizki.research.data.file.latex;

import ivanizki.research.data.ASCII;

/**
 * Interface to work with LaTeX.
 *
 * @author ivanizki
 */
@SuppressWarnings("javadoc")
public interface Latex {

	public static final String ARRAY = "array";

	public static final String ARRAY_ALIGN_CENTER = "c";

	public static final String BEGIN = "begin";

	public static final String CDOT = "cdot";

	public static final String END = "end";

	public static final String FRAC = "frac";

	public static final String SQRT = "sqrt";

	public static final String POWER = ASCII.HAT;

	public static final String CHOICE = ASCII.SEPARATOR;

	public static final String ARG_BEGIN = ASCII.PAR_BEGIN;

	public static final String ARG_END = ASCII.PAR_END;

	public static final String PARAM_BEGIN = ASCII.SQUARE_BRACKET_BEGIN;

	public static final String PARAM_END = ASCII.SQUARE_BRACKET_END;
}

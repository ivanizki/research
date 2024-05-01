package ivanizki.research.data.file.latex;

import ivanizki.research.data.ASCII;
import ivanizki.research.data.StringUtil;

/**
 * Utilities to work with {@link Latex}.
 *
 * @author ivanizki
 */
public class LatexUtil implements Latex {

	public static String cdot() {
		return macro(CDOT);
	}

	public static String power(String base, String exponent) {
		StringBuilder sb = new StringBuilder();
		sb.append(base);
		sb.append(POWER);
		if (exponent.length() > 1) {
			sb.append(argument(exponent));
		} else {
			sb.append(exponent);
		}
		return sb.toString();
	}

	public static String frac(String numerator, String denominator) {
		return macro(FRAC, new String[] { numerator, denominator }, null);
	}

	public static String sqrt(String arg) {
		StringBuilder sb = new StringBuilder();
		sb.append(macro(SQRT));
		sb.append(argument(arg));
		return sb.toString();
	}

	public static String array(String[][] values, int rows, int columns) {
		StringBuilder sb = new StringBuilder();
		String align = arrayAlign(columns);
		for (int row = 0; row < rows; row++) {
			for (int column = 0; column < columns; column++) {
				sb.append(values[row][column]);
				sb.append(ASCII.SPACE);
				if (column < columns - 1) {
					sb.append(ASCII.AMPERSAND);
					sb.append(ASCII.SPACE);
				}
			}
			if (row < rows - 1) {
				sb.append(ASCII.BACKSLASH);
				sb.append(ASCII.BACKSLASH);
				sb.append(ASCII.NEWLINE);
			}
		}
		return block(ARRAY, new String[] { align }, null, sb.toString());
	}

	private static String arrayAlign(int columns) {
		StringBuilder sb = new StringBuilder();
		for (int column = 0; column < columns; column++) {
			sb.append(ARRAY_ALIGN_CENTER);
		}
		return sb.toString();
	}

	public static String block(String name, String[] args, String[] params, String content) {
		StringBuilder sb = new StringBuilder();
		sb.append(macro(BEGIN));
		sb.append(argument(name));
		sb.append(arguments(args));
		sb.append(parameters(params));
		sb.append(ASCII.NEWLINE);
		sb.append(content);
		sb.append(ASCII.NEWLINE);
		sb.append(macro(END));
		sb.append(argument(name));
		return sb.toString();
	}

	public static String macro(String name) {
		StringBuilder sb = new StringBuilder();
		sb.append(macro(name, null, null));
		return sb.toString();
	}

	public static String macro(String name, String[] args, String[] params) {
		StringBuilder sb = new StringBuilder();
		sb.append(ASCII.BACKSLASH);
		sb.append(name);
		sb.append(arguments(args));
		sb.append(parameters(params));
		return sb.toString();
	}

	public static String arguments(String[] args) {
		if (args == null) {
			return StringUtil.EMPTY;
		}
		StringBuilder sb = new StringBuilder();
		for (String arg : args) {
			sb.append(argument(arg));
		}
		return sb.toString();
	}

	public static String argument(String arg) {
		StringBuilder sb = new StringBuilder();
		sb.append(ARG_BEGIN);
		sb.append(arg);
		sb.append(ARG_END);
		return sb.toString();
	}

	public static String parameters(String[] params) {
		if (params == null) {
			return StringUtil.EMPTY;
		}
		StringBuilder sb = new StringBuilder();
		for (String param : params) {
			sb.append(parameter(param));
		}
		return sb.toString();
	}

	public static String parameter(String param) {
		StringBuilder sb = new StringBuilder();
		sb.append(PARAM_BEGIN);
		sb.append(param);
		sb.append(PARAM_END);
		return sb.toString();
	}

}

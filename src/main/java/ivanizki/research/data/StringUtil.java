package ivanizki.research.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities to work with {@link String}s.
 *
 * @author ivanizki
 */
public class StringUtil {

	/** Empty {@link String}. */
	public static final String EMPTY = "";

	/**
	 * @return Whether the given {@link String} is empty or <code>null</code>.
	 */
	public static boolean isEmpty(String string) {
		return string == null || string.isEmpty();
	}

	/**
	 * Splits the given {@link String} around matches of the given {@link String regular
	 * expression}.
	 * 
	 * @return the resulting partial {@link String}s.
	 */
	public static List<String> split(String string, String regex) {
		List<String> parts = new ArrayList<>();
		if (string != null) {
			for (String part : string.split(regex)) {
				if (!isEmpty(part)) {
					parts.add(part);
				}
			}
		}
		return parts;
	}

	/**
	 * Joins the given partial {@link String}s via the given {@link String separator}.
	 * 
	 * @return the resulting joined {@link String}.
	 */
	public static String join(List<?> parts, String separator) {
		StringBuilder builder = new StringBuilder();
		int size = parts.size();
		for (int i = 0; i < size; i++) {
			builder.append(parts.get(i));
			if (i < size - 1) {
				builder.append(separator);
			}
		}
		return builder.toString();
	}

}

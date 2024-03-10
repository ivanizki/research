package ivanizki.research.data.file;

import com.top_logic.basic.util.ResKey;
import com.top_logic.layout.I18NConstantsBase;

/**
 * Internationalization constants for this package.
 *
 * @author ivanizki
 */
@SuppressWarnings("javadoc")
public class I18NConstants extends I18NConstantsBase {

	public static ResKey FAILED_TO_CREATE_FILE;

	public static ResKey FAILED_TO_FIND_FILE;

	public static ResKey FAILED_TO_READ_FROM_FILE;

	public static ResKey FAILED_TO_WRITE_TO_FILE;

	static {
		initConstants(I18NConstants.class);
	}
}

package ivanizki.research;

import com.top_logic.basic.ExceptionUtil;

/**
 * Dummy logger.
 * 
 * @author ivanizki
 */
public class DummyLogger {

	/**
	 * Writes error message to the standard error output stream.
	 */
	public static void error(Throwable throwable) {
		error(ExceptionUtil.getFullMessage(throwable));
	}

	/**
	 * Writes error message to the standard error output stream.
	 */
	public static void error(String message) {
		System.err.println(message);
	}
}

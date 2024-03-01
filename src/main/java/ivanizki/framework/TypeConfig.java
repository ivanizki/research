package ivanizki.framework;

import com.top_logic.basic.config.ConfigurationItem;
import com.top_logic.basic.config.annotation.Name;

/**
 * {@link ConfigurationItem} for {@link String type} corresponding to this {@link Object}.
 *
 * @author ivanizki
 */
public interface TypeConfig extends ConfigurationItem {

	/** @see #getType() */
	String TYPE = "type";

	/**
	 * @return The {@link String type} corresponding to this {@link Object}.
	 */
	@Name(TYPE)
	String getType();
}

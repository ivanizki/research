package ivanizki.research.model;

import java.util.Collection;

import com.top_logic.basic.CollectionUtil;
import com.top_logic.knowledge.wrap.ValueProvider;

/**
 * Utilities to work with {@link ValueProvider}s.
 *
 * @author ivanizki
 */
public class ValueProviderUtil {

	/**
	 * @return The attribute value.
	 */
	public static Object getValue(Object valueProvider, String attributeName) {
		return ((ValueProvider) valueProvider).getValue(attributeName);
	}

	/**
	 * @return The attribute values as a collection.
	 */
	public static Collection<?> getValues(Object valueProvider, String attributeName) {
		return (Collection<?>) getValue(valueProvider, attributeName);
	}

	/**
	 * @return The attribute values as a collection of {@link ValueProvider}s.
	 */
	public static Collection<ValueProvider> getValueProviders(Object valueProvider, String attributeName) {
		return CollectionUtil.dynamicCastView(ValueProvider.class, getValues(valueProvider, attributeName));
	}
}

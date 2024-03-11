package ivanizki.research.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.top_logic.basic.CollectionUtil;
import com.top_logic.basic.StringServices;
import com.top_logic.basic.col.MapUtil;
import com.top_logic.element.meta.MetaElementUtil;
import com.top_logic.element.meta.TypeSpec;
import com.top_logic.knowledge.wrap.ValueProvider;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.model.TLClass;
import com.top_logic.model.TLModuleSingleton;
import com.top_logic.model.TLStructuredTypePart;
import com.top_logic.model.TLType;
import com.top_logic.model.util.TLModelUtil;

/**
 * Utilities to work with {@link Model}, {@link ModelType} and {@link ModelModule}.
 *
 * @author ivanizki
 */
public class ModelUtil {

	@SuppressWarnings("javadoc")
	public static final TLClass DATA_ITEM_TYPE = (TLClass) TLModelUtil.findType(ModelModule.RESEARCH, ModelType.DATA_ITEM);
	
	/**
	 * @return All {@link TLClass type}s in the database that are {@link #isRelevantType(TLClass)
	 *         relevant}.
	 */
	public static Set<TLClass> getRelevantTypes() {
		return TLModelUtil.getConcreteSpecializations(DATA_ITEM_TYPE);
	}

	/**
	 * @return All {@link TLStructuredTypePart attributes} of the given {@link TLClass type} that
	 *         are {@link #isRelevantType(TLClass) relevant}.
	 */
	public static List<TLStructuredTypePart> getOwnAttributes(TLClass type) {
		List<TLStructuredTypePart> attributes = new ArrayList<>();
		for (TLStructuredTypePart attribute : type.getAllParts()) {
			if (ModelUtil.isRelevantType((TLClass) attribute.getOwner())) {
				attributes.add(attribute);
			}
		}
		return attributes;
	}

	/**
	 * @return Whether the given {@link TLClass type} is relevant, that is, instance of
	 *         {@link ModelType#DATA_ITEM}.
	 */
	public static boolean isRelevantType(TLClass type) {
		return MetaElementUtil.isSubtype(ModelModule.RESEARCH, ModelType.DATA_ITEM, type);
	}

	/**
	 * @return All {@link Wrapper singleton}s for the given relevant {@link TLClass type}s.
	 */
	public static Map<TLClass, Wrapper> getRelevantSingletons(Collection<TLClass> relevantTypes) {
		Map<TLClass, Wrapper> singletons = new HashMap<>();
		for (TLClass type : relevantTypes) {
			for (TLModuleSingleton singleton : type.getModule().getSingletons()) {
				Wrapper wrapper = (Wrapper) singleton.getSingleton();
				TLClass singletonType = (TLClass) wrapper.tType();
				singletons.put(singletonType, wrapper);
			}
		}
		return singletons;
	}

	/**
	 * @return The value parsed from the given {@link String} according to the given {@link TLType}
	 *         of value.
	 */
	public static Object parse(TLType type, String string) {
		if (TypeSpec.STRING_TYPE.equals(type.toString())) {
			return string;
		}
		if (!StringServices.isEmpty(string)) {
			if (TypeSpec.INTEGER_TYPE.equals(type.toString())) {
				return Integer.parseInt(string);
			} else if (TypeSpec.LONG_TYPE.equals(type.toString())) {
				return Long.parseLong(string);
			} else if (TypeSpec.DOUBLE_TYPE.equals(type.toString())) {
				return Double.parseDouble(string);
			} else if (TypeSpec.BOOLEAN_TYPE.equals(type.toString())) {
				return Boolean.parseBoolean(string);
			}
		}
		return null;
	}

	/**
	 * @return All {@link Wrapper}s of the given {@link TLClass type}.
	 */
	public static List<Wrapper> getAllWrappers(TLClass type) {
		return getAllWrappers(type, false);
	}

	/**
	 * @return All {@link Wrapper}s of the given {@link TLClass type}, eventually including
	 *         sub-types.
	 */
	public static List<Wrapper> getAllWrappers(TLClass type, boolean includeSubTypes) {
		if (includeSubTypes) {
			return MetaElementUtil.getAllInstancesOf(type, Wrapper.class);
		}
		return MetaElementUtil.getAllDirectInstancesOf(type, Wrapper.class);
	}

	/**
	 * @return The given {@link ValueProvider}s indexed by values of the specified attribute.
	 */
	public static <T extends ValueProvider> Map<String, T> indexByAttribute(Collection<T> wrappers, String attributeName) {
		return MapUtil.createValueMap(wrappers, wrapper -> (String) wrapper.getValue(attributeName));
	}

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

	/**
	 * @return The {@link Model#CHILDREN} as a collection of {@link ValueProvider}s.
	 */
	public static Set<ValueProvider> getChildrenRecursively(Object valueProvider) {
		Set<ValueProvider> topics = new HashSet<>();
		collectChildrenRecursively((ValueProvider) valueProvider, topics);
		return topics;
	}

	private static void collectChildrenRecursively(ValueProvider parent, Set<ValueProvider> children) {
		children.add(parent);
		for (ValueProvider child : ModelUtil.getValueProviders(parent, Model.CHILDREN)) {
			collectChildrenRecursively(child, children);
		}
	}
}

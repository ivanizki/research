package ivanizki.research.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.top_logic.element.meta.MetaElementUtil;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.model.TLClass;
import com.top_logic.model.TLModuleSingleton;
import com.top_logic.model.util.TLModelUtil;

/**
 * Utilities to work with database.
 *
 * @author ivanizki
 */
public class DataUtil {

	/** ID column. */
	public static final String ID = "id";

	private static final String RELEVANT_MODULE_NAME = "research";

	private static final String RELEVANT_TYPE_NAME = "DataItem";

	/**
	 * @return All {@link TLClass type}s in the database that are relevant.
	 */
	public static Set<TLClass> getRelevantTypes() {
		TLClass generalType = (TLClass) TLModelUtil.findType(RELEVANT_MODULE_NAME, RELEVANT_TYPE_NAME);
		return TLModelUtil.getConcreteSpecializations(generalType);
	}

	/**
	 * @return Whether the given {@link TLClass type} is relevant.
	 */
	public static boolean isRelevantType(TLClass type) {
		return MetaElementUtil.isSubtype(RELEVANT_MODULE_NAME, RELEVANT_TYPE_NAME, type);
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
}

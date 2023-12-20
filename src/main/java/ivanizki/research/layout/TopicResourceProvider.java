package ivanizki.research.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.top_logic.basic.CollectionUtil;
import com.top_logic.basic.StringServices;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.AbstractResourceProvider;

import ivanizki.research.data.ASCII;
import ivanizki.research.model.ModelUtil;
import ivanizki.research.model.Model;
import ivanizki.research.model.ModelType;

/**
 * {@link AbstractResourceProvider} for {@link ModelType#TOPIC}s.
 *
 * @author ivanizki
 */
public class TopicResourceProvider extends AbstractResourceProvider {

	private static final String SEPARATOR = new String(new char[] { ASCII.SPACE, ASCII.GREATER, ASCII.SPACE });

	@Override
	public String getLabel(Object object) {
		return getName(object);
	}

	@Override
	public String getTooltip(Object object) {
		List<String> names = new ArrayList<>();
		names.add(getName(object));
		boolean escape = false;
		while (!escape) {
			Collection<?> parents = ModelUtil.getValues(object, Model.PARENTS);
			if (parents.size() == 1) {
				object = CollectionUtil.getFirst(parents);
				names.add(getName(object));
			} else {
				escape = true;
			}
		}
		Collections.reverse(names);
		return StringServices.join(names, SEPARATOR);
	}

	private String getName(Object object) {
		return ((Wrapper) object).getName();
	}

}

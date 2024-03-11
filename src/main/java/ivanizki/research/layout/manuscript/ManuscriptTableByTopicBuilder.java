package ivanizki.research.layout.manuscript;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.top_logic.basic.CollectionUtil;
import com.top_logic.basic.StringServices;
import com.top_logic.element.meta.MetaElementUtil;
import com.top_logic.knowledge.wrap.ValueProvider;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.table.component.TableComponent;
import com.top_logic.mig.html.ListModelBuilder;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.model.TLClass;
import com.top_logic.model.TLReference;
import com.top_logic.model.util.TLModelUtil;

import ivanizki.research.model.Model;
import ivanizki.research.model.ModelModule;
import ivanizki.research.model.ModelType;
import ivanizki.research.model.ModelUtil;

/**
 * {@link ListModelBuilder} building the table of {@link ModelType#MANUSCRIPT}s from the given
 * {@link ModelType#TOPIC}s.
 *
 * @author ivanizki
 */
public class ManuscriptTableByTopicBuilder implements ListModelBuilder {

	@Override
	public Collection<?> getModel(Object businessModel, LayoutComponent component) {
		Collection<?> topics = businessModel instanceof Collection<?> ? (Collection<?>) businessModel : CollectionUtil.intoSetNotNull(businessModel);
		if (!topics.isEmpty()) {
			Set<Object> manuscripts = new HashSet<>();
			TLReference reference = (TLReference) ModelUtil.DATA_ITEM_TYPE.getPart(Model.TOPICS);
			for (Object topic : topics) {
				for (ValueProvider subTopic : ModelUtil.getChildrenRecursively(topic)) {
					for (Object referer : ((Wrapper) subTopic).tReferers(reference)) {
						if (supportsListElement(component, referer)) {
							manuscripts.add(referer);
						}
					}
				}
			}
			return CollectionUtil.toList(manuscripts);
		}
		return Collections.emptyList();
	}

	@Override
	public boolean supportsModel(Object model, LayoutComponent component) {
		if (model instanceof Collection<?>) {
			return supportsMultipleSelection((Collection<?>) model);
		}
		return supportsSingleSelection(model);
	}

	private boolean supportsMultipleSelection(Collection<?> objects) {
		return objects.isEmpty() || supportsSingleSelection(CollectionUtil.getFirst(objects));
	}

	private boolean supportsSingleSelection(Object object) {
		return object instanceof Wrapper && MetaElementUtil.isSubtype(ModelModule.TOPICS, ModelType.TOPIC, (TLClass) ((Wrapper) object).tType());
	}

	@Override
	public boolean supportsListElement(LayoutComponent component, Object candidate) {
		if (candidate instanceof Wrapper) {
			Wrapper wrapper = (Wrapper) candidate;
			if (wrapper.tValid()) {
				List<String> typeNames = ((TableComponent.Config) ((TableComponent) component).getConfig()).getObjectType();
				for (String typeName : typeNames) {
					String[] typeNameParts = StringServices.split(typeName, TLModelUtil.QUALIFIED_NAME_SEPARATOR);
					if (MetaElementUtil.isSubtype(typeNameParts[0], typeNameParts[1], (TLClass) wrapper.tType())) {
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public Object retrieveModelFromListElement(LayoutComponent component, Object candidate) {
		return component.getModel();
	}

}

package ivanizki.research.layout.manuscript.dialogs.create;

import java.util.Map;

import com.top_logic.basic.CollectionUtil;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.element.model.DynamicModelService;
import com.top_logic.knowledge.service.PersistencyLayer;
import com.top_logic.knowledge.service.Transaction;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.DisplayContext;
import com.top_logic.layout.form.FormField;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.model.TLClass;
import com.top_logic.model.TLStructuredTypePart;
import com.top_logic.tool.boundsec.AbstractCommandHandler;
import com.top_logic.tool.boundsec.HandlerResult;

import ivanizki.research.layout.manuscript.ArticleFormComponent;
import ivanizki.research.model.ModelType;
import ivanizki.research.model.ModelUtil;

/**
 * {@link AbstractCommandHandler} to create {@link ModelType#ARTICLE}s.
 *
 * @author ivanizki
 */
public class ArticleCreateHandler extends AbstractCommandHandler {

	/**
	 * Creates a new {@link ArticleCreateHandler}.
	 */
	public ArticleCreateHandler(InstantiationContext context, Config config) {
		super(context, config);
	}

	@Override
	public HandlerResult handleCommand(DisplayContext context, LayoutComponent component, Object model,	Map<String, Object> someArguments) {
		ArticleFormComponent form = (ArticleFormComponent) component;
		TLClass type = form.getType();
		Transaction transaction = PersistencyLayer.getKnowledgeBase().beginTransaction();
		try {
			Wrapper wrapper = (Wrapper) DynamicModelService.getInstance().createObject(type);
			for (TLStructuredTypePart attribute : ModelUtil.getOwnAttributes(type)) {
				if (!attribute.isDerived()) {
					String attributeName = attribute.getName();
					FormField field = form.getFormContext().getField(attributeName);
					Object fieldValue = field.getValue();
					Object attributeValue = attribute.isMultiple() ? fieldValue : CollectionUtil.getSingleValueFrom(fieldValue);
					wrapper.setValue(attributeName, attributeValue);
				}
			}
			transaction.commit();
		} finally {
			transaction.rollback();
		}
		component.closeDialog();
		return HandlerResult.DEFAULT_RESULT;
	}
	
}

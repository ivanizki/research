package ivanizki.framework;

import java.util.Map;

import com.top_logic.basic.CollectionUtil;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.knowledge.service.PersistencyLayer;
import com.top_logic.knowledge.service.Transaction;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.DisplayContext;
import com.top_logic.layout.form.FormField;
import com.top_logic.layout.form.component.FormComponent;
import com.top_logic.layout.form.model.FormContext;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.model.TLClass;
import com.top_logic.model.TLStructuredTypePart;
import com.top_logic.tool.boundsec.AbstractCommandHandler;
import com.top_logic.tool.boundsec.HandlerResult;

import ivanizki.research.model.ModelUtil;

/**
 * {@link AbstractCommandHandler} to {@link #applyChanges} done to {@link Wrapper}.
 *
 * @author ivanizki
 */
public class WrapperApplyHandler extends AbstractCommandHandler {

	/**
	 * Creates a new {@link WrapperApplyHandler}.
	 */
	public WrapperApplyHandler(InstantiationContext context, Config config) {
		super(context, config);
	}

	@Override
	public HandlerResult handleCommand(DisplayContext context, LayoutComponent component, Object model,	Map<String, Object> someArguments) {
		Transaction transaction = PersistencyLayer.getKnowledgeBase().beginTransaction();
		try {
			applyChanges(context, (FormComponent) component, (Wrapper) model, someArguments);
			transaction.commit();
		} finally {
			transaction.rollback();
		}
		return HandlerResult.DEFAULT_RESULT;
	}
	
	/**
	 * Applies changes to the given {@link Wrapper}.
	 */
	@SuppressWarnings("unused")
	protected void applyChanges(DisplayContext context, FormComponent formComponent, Wrapper wrapper, Map<String, Object> someArguments) {
		FormContext formContext = formComponent.getFormContext();
		for (TLStructuredTypePart attribute : ModelUtil.getOwnAttributes((TLClass) wrapper.tType())) {
			if (!attribute.isDerived()) {
				String attributeName = attribute.getName();
				FormField field = formContext.getField(attributeName);
				Object fieldValue = field.getValue();
				Object attributeValue = attribute.isMultiple() ? fieldValue : CollectionUtil.getSingleValueFrom(fieldValue);
				wrapper.setValue(attributeName, attributeValue);
			}
		}
	}

}

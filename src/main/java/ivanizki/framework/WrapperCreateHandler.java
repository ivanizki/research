package ivanizki.framework;

import java.util.Map;

import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.element.model.DynamicModelService;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.DisplayContext;
import com.top_logic.layout.form.component.FormComponent;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.tool.boundsec.HandlerResult;

/**
 * {@link WrapperApplyHandler} creating the {@link Wrapper}.
 *
 * @author ivanizki
 */
public class WrapperCreateHandler extends WrapperApplyHandler {

	/**
	 * Creates a new {@link WrapperCreateHandler}.
	 */
	public WrapperCreateHandler(InstantiationContext context, Config config) {
		super(context, config);
	}

	@Override
	public HandlerResult handleCommand(DisplayContext context, LayoutComponent component, Object model,	Map<String, Object> someArguments) {
		HandlerResult result = super.handleCommand(context, component, model, someArguments);
		component.closeDialog();
		return result;
	}
	
	@Override
	protected void applyChanges(DisplayContext context, FormComponent formComponent, Wrapper wrapper, Map<String, Object> someArguments) {
		wrapper = (Wrapper) DynamicModelService.getInstance().createObject(((TypeBased) formComponent).getType());
		super.applyChanges(context, formComponent, wrapper, someArguments);
	}

}

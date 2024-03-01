package ivanizki.framework;

import java.util.Map;

import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.layout.DisplayContext;
import com.top_logic.layout.form.component.edit.EditMode;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.tool.boundsec.HandlerResult;

/**
 * {@link WrapperApplyHandler} switching to the view mode.
 *
 * @author ivanizki
 */
public class WrapperSaveHandler extends WrapperApplyHandler {

	/**
	 * Creates a new {@link WrapperSaveHandler}.
	 */
	public WrapperSaveHandler(InstantiationContext context, Config config) {
		super(context, config);
	}

	@Override
	public HandlerResult handleCommand(DisplayContext context, LayoutComponent component, Object model,	Map<String, Object> someArguments) {
		HandlerResult result = super.handleCommand(context, component, model, someArguments);
		if (result.isSuccess() && component instanceof EditMode) {
			((EditMode) component).setViewMode();
		}
		return result;
	}

}

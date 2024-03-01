package ivanizki.framework;

import java.util.Map;

import com.top_logic.layout.form.component.FormComponent;
import com.top_logic.layout.form.model.FormContext;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.tool.execution.ExecutabilityRule;
import com.top_logic.tool.execution.ExecutableState;

/**
 * {@link ExecutabilityRule} to {@link FormContext#checkAllConstraints check all constraints} in
 * this {@link FormComponent}.
 *
 * @author ivanizki
 */
public class FormConstraintsCheckingRule implements ExecutabilityRule {

	@Override
	public ExecutableState isExecutable(LayoutComponent component, Object model, Map<String, Object> someValues) {
		FormComponent form = (FormComponent) component;
		if (!form.getFormContext().checkAllConstraints()) {
			return ExecutableState.NOT_EXEC_DISABLED;
		}
		return ExecutableState.EXECUTABLE;
	}
}

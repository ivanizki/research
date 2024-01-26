package ivanizki.research.layout.manuscript.dialogs.create;

import java.util.Map;

import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.tool.execution.ExecutabilityRule;
import com.top_logic.tool.execution.ExecutableState;

import ivanizki.research.layout.manuscript.ArticleFormComponent;
import ivanizki.research.model.ModelType;

/**
 * {@link ExecutabilityRule} to create {@link ModelType#ARTICLE}s.
 *
 * @author ivanizki
 */
public class ArticleCreateRule implements ExecutabilityRule {

	@Override
	public ExecutableState isExecutable(LayoutComponent component, Object model, Map<String, Object> someValues) {
		ArticleFormComponent form = (ArticleFormComponent) component;
		if (!form.getFormContext().checkAllConstraints()) {
			return ExecutableState.NOT_EXEC_DISABLED;
		}
		return ExecutableState.EXECUTABLE;
	}
}

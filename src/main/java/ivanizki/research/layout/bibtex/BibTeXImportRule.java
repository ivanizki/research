package ivanizki.research.layout.bibtex;

import java.util.Map;

import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.tool.execution.ExecutabilityRule;
import com.top_logic.tool.execution.ExecutableState;

import ivanizki.research.data.file.bibtex.BibTeX;

/**
 * {@link ExecutabilityRule} for {@link BibTeX} import.
 *
 * @author ivanizki
 */
public class BibTeXImportRule implements ExecutabilityRule {

	@Override
	public ExecutableState isExecutable(LayoutComponent component, Object model, Map<String, Object> someValues) {
		if (((BibTeXImportDialog) component).getData() == null) {
			return ExecutableState.createDisabledState(I18NConstants.BIBTEX_IMPORT_DISABLED_DUE_NO_FILE_SELECTED);
		}
		return ExecutableState.EXECUTABLE;
	}
}

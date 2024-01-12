package ivanizki.research.layout.manuscript;

import com.top_logic.knowledge.gui.WrapperResourceProvider;

import ivanizki.research.data.ASCII;
import ivanizki.research.model.Model;
import ivanizki.research.model.ModelType;
import ivanizki.research.model.ModelUtil;

/**
 * {@link WrapperResourceProvider} for {@link ModelType#MANUSCRIPT}s.
 *
 * @author ivanizki
 */
public class ManuscriptResourceProvider extends WrapperResourceProvider {

	@Override
	public String getLabel(Object object) {
		return new StringBuilder()
			.append(ASCII.QUOTE)
			.append((String) ModelUtil.getValue(object, Model.TITLE))
			.append(ASCII.QUOTE)
			.toString();
	}

}

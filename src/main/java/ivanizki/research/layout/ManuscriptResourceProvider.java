package ivanizki.research.layout;

import com.top_logic.layout.AbstractResourceProvider;

import ivanizki.research.data.ASCII;
import ivanizki.research.model.ModelUtil;
import ivanizki.research.model.Model;
import ivanizki.research.model.ModelType;

/**
 * {@link AbstractResourceProvider} for {@link ModelType#MANUSCRIPT}s.
 *
 * @author ivanizki
 */
public class ManuscriptResourceProvider extends AbstractResourceProvider {

	@Override
	public String getLabel(Object object) {
		return new StringBuilder()
			.append(ASCII.QUOTE)
			.append((String) ModelUtil.getValue(object, Model.TITLE))
			.append(ASCII.QUOTE)
			.toString();
	}

}

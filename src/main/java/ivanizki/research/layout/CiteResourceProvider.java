package ivanizki.research.layout;

import com.top_logic.layout.AbstractResourceProvider;

import ivanizki.research.model.Model;
import ivanizki.research.model.ModelType;
import ivanizki.research.model.ModelUtil;

/**
 * {@link AbstractResourceProvider} for {@link ModelType#CITE}s.
 *
 * @author ivanizki
 */
public class CiteResourceProvider extends AbstractResourceProvider {

	@Override
	public String getLabel(Object object) {
		return (String) ModelUtil.getValue(object, Model.TEXT);
	}

}

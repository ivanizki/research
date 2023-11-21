package ivanizki.research.layout;

import com.top_logic.layout.AbstractResourceProvider;

import ivanizki.research.data.ASCII;
import ivanizki.research.model.Manuscript;
import ivanizki.research.model.ValueProviderUtil;

/**
 * {@link AbstractResourceProvider} for {@link Manuscript}s.
 *
 * @author ivanizki
 */
public class ManuscriptResourceProvider extends AbstractResourceProvider {

	@Override
	public String getLabel(Object object) {
		String title = (String) ValueProviderUtil.getValue(object, Manuscript.TITLE);
		return ASCII.QUOTE + title + ASCII.QUOTE;
	}

}

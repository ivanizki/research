package ivanizki.research;

import javax.servlet.ServletContext;

import com.top_logic.basic.StringServices;
import com.top_logic.element.ElementStartStop;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.model.util.TLModelUtil;

import ivanizki.research.model.Model;
import ivanizki.research.model.ModelModule;

/**
 * {@link ElementStartStop} for research.
 *
 * @author ivanizki
 */
public class ResearchStartStop extends ElementStartStop {

	private static final String ROOT = "ROOT";

	private static final String TOPICS_ROOT = "Topics";

	@Override
	protected void initSubClassHook(ServletContext servletContext) throws Exception {
		super.initSubClassHook(servletContext);
		initTopicsRootName();
	}

	private void initTopicsRootName() {
		Wrapper topicsRoot = (Wrapper) TLModelUtil.findSingleton(ModelModule.TOPICS, ROOT);
		if (!StringServices.equals(TOPICS_ROOT, topicsRoot.getValue(Model.NAME))) {
			topicsRoot.setValue(Model.NAME, TOPICS_ROOT);
		}
	}
}

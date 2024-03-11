package ivanizki.research.layout.manuscript;

import java.util.HashSet;
import java.util.Set;

import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.form.CheckException;
import com.top_logic.layout.form.constraints.AbstractConstraint;
import com.top_logic.model.TLClass;
import com.top_logic.model.util.TLModelUtil;

import ivanizki.research.model.Model;
import ivanizki.research.model.ModelModule;
import ivanizki.research.model.ModelType;
import ivanizki.research.model.ModelUtil;

/**
 * {@link AbstractConstraint} that checks the uniqueness of {@link Model#TITLE} of a
 * {@link ModelType#MANUSCRIPT}.
 *
 * @author ivanizki
 */
public class ManuscriptTitleUniquenessConstraint extends AbstractConstraint {

	private Set<String> _allTitles;

	/**
	 * Creates a new {@link ManuscriptTitleUniquenessConstraint}.
	 */
	public ManuscriptTitleUniquenessConstraint() {
		super();
		_allTitles = initAllTitles();
	}

	private Set<String> initAllTitles() {
		Set<String> titles = new HashSet<>();
		for (Wrapper manuscript : ModelUtil.getAllWrappers((TLClass) TLModelUtil.findType(ModelModule.ARTWORKS, ModelType.MANUSCRIPT), true)) {
			titles.add((String) manuscript.getValue(Model.TITLE));
		}
		return titles;
	}

	@Override
	public boolean check(Object value) throws CheckException {
		if (_allTitles.contains(value)) {
			throw new CheckException(I18NConstants.MANUSCRIPT_TITLE_NOT_UNIQUE);
		}
		return true;
	}

}

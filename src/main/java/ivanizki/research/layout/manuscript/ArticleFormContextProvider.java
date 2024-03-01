package ivanizki.research.layout.manuscript;

import com.top_logic.layout.form.FormField;
import com.top_logic.layout.form.model.FormFactory;
import com.top_logic.layout.form.values.MultiLineText;

import ivanizki.framework.TypeBasedFormContextProvider;
import ivanizki.research.model.Model;
import ivanizki.research.model.ModelType;

/**
 * {@link TypeBasedFormContextProvider} for {@link ModelType#ARTICLE}.
 *
 * @author ivanizki
 */
public class ArticleFormContextProvider extends TypeBasedFormContextProvider {

	/**
	 * Creates a new {@link ArticleFormContextProvider}.
	 */
	public ArticleFormContextProvider() {
		super();
	}

	@Override
	protected void initField(FormField field) {
		if (Model.TITLE.equals(field.getName())) {
			field.setMandatory(FormFactory.MANDATORY);
		} else if (Model.ABSTRACT.equals(field.getName())) {
			field.setControlProvider(MultiLineText.INSTANCE);
		}
	}

}

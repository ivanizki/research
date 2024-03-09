package ivanizki.research.layout.manuscript;

import com.top_logic.basic.StringServices;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.Control;
import com.top_logic.layout.form.FormField;
import com.top_logic.layout.form.FormMember;
import com.top_logic.layout.form.component.edit.EditMode;
import com.top_logic.layout.form.control.SelectionControl;
import com.top_logic.layout.form.model.FormContext;
import com.top_logic.layout.form.model.FormFactory;
import com.top_logic.layout.form.model.SelectField;
import com.top_logic.layout.form.template.SelectionControlProvider;
import com.top_logic.layout.form.treetable.component.ConstantFieldProvider;
import com.top_logic.layout.form.values.MultiLineText;
import com.top_logic.layout.provider.MetaLabelProvider;
import com.top_logic.mig.html.layout.LayoutComponent;

import ivanizki.framework.TypeBasedFormContextProvider;
import ivanizki.research.model.Model;
import ivanizki.research.model.ModelType;

/**
 * {@link TypeBasedFormContextProvider} for {@link ModelType#ARTICLE}.
 *
 * @author ivanizki
 */
public class ArticleFormContextProvider extends TypeBasedFormContextProvider {

	/** DOI host. */
	private static final String DOI_HOST = "https://doi.org/";

	/** Representation of {@link Model#DOI} as a HTML link. */
	public static final String DOI_LINK = "doiLink";

	/**
	 * Creates a new {@link ArticleFormContextProvider}.
	 */
	public ArticleFormContextProvider() {
		super();
	}

	@Override
	public FormContext createFormContext(LayoutComponent component) {
		FormContext formContext = super.createFormContext(component);

		if (component instanceof EditMode && ((EditMode) component).isInViewMode()) {
			FormMember field = createDoiLinkField((Wrapper) component.getModel());
			formContext.addMember(field);
		}

		return formContext;
	}

	private FormMember createDoiLinkField(Wrapper article) {
		String doi = (String) article.getValue(Model.DOI);
		Object link = StringServices.isEmpty(doi) ? StringServices.EMPTY_STRING : DOI_HOST + doi;
		FormMember field = ConstantFieldProvider.INSTANCE.createField(DOI_LINK, link);
		field.setLabel(MetaLabelProvider.INSTANCE.getLabel(article.tType().getPart(Model.DOI)));
		field.setControlProvider(new SelectionControlProvider() {
			@Override
			public Control visitSelectField(SelectField member, Void arg) {
				SelectionControl control = (SelectionControl) super.visitSelectField(member, arg);
				control.setOptionRenderer(DoiLinkRenderer.INSTANCE);
				return control;
			}
		});
		return field;
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

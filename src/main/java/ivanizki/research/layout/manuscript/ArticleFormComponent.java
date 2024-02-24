package ivanizki.research.layout.manuscript;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.top_logic.basic.config.ConfigurationException;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.element.meta.TypeSpec;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.form.FormField;
import com.top_logic.layout.form.component.FormComponent;
import com.top_logic.layout.form.model.FormContext;
import com.top_logic.layout.form.model.FormFactory;
import com.top_logic.layout.form.model.SelectField;
import com.top_logic.layout.form.template.SelectionControlProvider;
import com.top_logic.layout.form.values.MultiLineText;
import com.top_logic.layout.provider.MetaLabelProvider;
import com.top_logic.model.TLClass;
import com.top_logic.model.TLReference;
import com.top_logic.model.TLStructuredTypePart;
import com.top_logic.model.TLType;
import com.top_logic.model.util.TLModelUtil;

import ivanizki.research.model.Model;
import ivanizki.research.model.ModelModule;
import ivanizki.research.model.ModelType;
import ivanizki.research.model.ModelUtil;

/**
 * {@link FormComponent} for {@link ModelType#ARTICLE}.
 *
 * @author ivanizki
 */
public class ArticleFormComponent extends FormComponent {

	private static final TLClass ARTICLE_TYPE = (TLClass) TLModelUtil.findType(ModelModule.ARTWORKS, ModelType.ARTICLE);

	/**
	 * Creates a new {@link ArticleFormComponent}.
	 */
	public ArticleFormComponent(InstantiationContext context, Config config) throws ConfigurationException {
		super(context, config);
	}

	@Override
	public FormContext createFormContext() {
		FormContext formContext = new FormContext(this);

		for (TLStructuredTypePart attribute : ModelUtil.getOwnAttributes(getType())) {
			addField(attribute, formContext);
		}

		return formContext;
	}

	/**
	 * @return The {@link TLClass type} of the created {@link Object}.
	 */
	public TLClass getType() {
		return ARTICLE_TYPE;
	}

	private void addField(TLStructuredTypePart attribute, FormContext formContext) {
		FormField field = createField(attribute);
		if (field != null) {
			field.setLabel(MetaLabelProvider.INSTANCE.getLabel(attribute));
			if (getModel() != null) {
				String attributeName = attribute.getName();
				Object attributeValue = ModelUtil.getValue(getModel(), attributeName);
				field.setValue(attributeValue);
			}
			initField(field);
		}
		formContext.addMember(field);
	}

	private FormField createField(TLStructuredTypePart attribute) {
		String attributeName = attribute.getName();
		TLType type = attribute.getType();
		if (TypeSpec.STRING_TYPE.equals(type.toString())) {
			return FormFactory.newStringField(attributeName);
		} else if (TypeSpec.INTEGER_TYPE.equals(type.toString())) {
			return FormFactory.newIntField(attributeName);
		} else if (TypeSpec.LONG_TYPE.equals(type.toString())) {
			return FormFactory.newLongField(attributeName, 0, !IMMUTABLE);
		} else if (TypeSpec.DOUBLE_TYPE.equals(type.toString())) {
			return FormFactory.newDoubleField(attributeName, 0, !IMMUTABLE);
		} else if (TypeSpec.BOOLEAN_TYPE.equals(type.toString())) {
			return FormFactory.newBooleanField(attributeName);
		} else if (attribute instanceof TLReference) {
			SelectField field = FormFactory.newSelectField(attributeName, getSelectFieldOptions(attribute), attribute.isMultiple(), !IMMUTABLE);
			field.setControlProvider(SelectionControlProvider.INSTANCE);
			field.setCustomOrder(attribute.isOrdered());
			return field;
		}
		return null;
	}

	private Collection<Wrapper> getSelectFieldOptions(TLStructuredTypePart attribute) {
		List<Wrapper> wrappers = ModelUtil.getAllWrappers((TLClass) attribute.getType());
		return attribute.isOrdered() ? wrappers : new HashSet<>(wrappers);
	}

	private void initField(FormField field) {
		if (Model.TITLE.equals(field.getName())) {
			field.setMandatory(MANDATORY);
		} else if (Model.ABSTRACT.equals(field.getName())) {
			field.setControlProvider(MultiLineText.INSTANCE);
		}
	}

}

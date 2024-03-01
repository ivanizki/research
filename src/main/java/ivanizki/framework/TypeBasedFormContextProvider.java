package ivanizki.framework;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import com.top_logic.basic.CollectionUtil;
import com.top_logic.element.meta.TypeSpec;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.form.FormField;
import com.top_logic.layout.form.model.FormContext;
import com.top_logic.layout.form.model.FormFactory;
import com.top_logic.layout.form.model.SelectField;
import com.top_logic.layout.form.template.SelectionControlProvider;
import com.top_logic.layout.provider.MetaLabelProvider;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.model.TLClass;
import com.top_logic.model.TLReference;
import com.top_logic.model.TLStructuredTypePart;
import com.top_logic.model.TLType;

import ivanizki.research.model.ModelUtil;

/**
 * Provides the {@link FormContext} for the given {@link TypeBased} {@link LayoutComponent}.
 *
 * @author ivanizki
 */
public abstract class TypeBasedFormContextProvider {

	/**
	 * @return The {@link TypeBased} {@link FormContext} for the given {@link LayoutComponent}.
	 */
	public FormContext createFormContext(LayoutComponent component) {
		FormContext formContext = new FormContext(component);

		for (TLStructuredTypePart attribute : ModelUtil.getOwnAttributes(((TypeBased) component).getType())) {
			addField(component.getModel(), attribute, formContext);
		}

		return formContext;
	}

	private void addField(Object model, TLStructuredTypePart attribute, FormContext formContext) {
		FormField field = createField(attribute);
		if (field != null) {
			field.setLabel(MetaLabelProvider.INSTANCE.getLabel(attribute));
			if (model != null) {
				String attributeName = attribute.getName();
				Object attributeValue = ModelUtil.getValue(model, attributeName);
				if (field instanceof SelectField && !(attributeValue instanceof Collection<?>)) {
					field.setValue(attribute.isOrdered() ? CollectionUtil.list(attributeValue)
						: CollectionUtil.set(attributeValue));
				} else {
					field.setValue(attributeValue);
				}
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
			return FormFactory.newLongField(attributeName, 0, !FormFactory.IMMUTABLE);
		} else if (TypeSpec.DOUBLE_TYPE.equals(type.toString())) {
			return FormFactory.newDoubleField(attributeName, 0, !FormFactory.IMMUTABLE);
		} else if (TypeSpec.BOOLEAN_TYPE.equals(type.toString())) {
			return FormFactory.newBooleanField(attributeName);
		} else if (attribute instanceof TLReference) {
			SelectField field = FormFactory.newSelectField(attributeName, getSelectFieldOptions(attribute),
				attribute.isMultiple(), !FormFactory.IMMUTABLE);
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

	/**
	 * Initializes the given {@link FormField} after all {@link TypeBased} values are set.
	 */
	protected abstract void initField(FormField field);

}

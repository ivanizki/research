package ivanizki.framework;

import com.top_logic.basic.config.ConfigurationItem;
import com.top_logic.basic.config.PolymorphicConfiguration;
import com.top_logic.basic.config.annotation.Name;
import com.top_logic.layout.form.component.FormComponent;

/**
 * {@link ConfigurationItem} for {@link TypeBasedFormContextProvider} corresponding to this
 * {@link TypeBased} {@link FormComponent}.
 *
 * @author ivanizki
 */
public interface FormContextProviderConfig extends ConfigurationItem {

	/** @see #getFormContextProvider() */
	String FORM_CONTEXT_PROVIDER = "form-context-provider";

	/**
	 * @return The {@link TypeBasedFormContextProvider} corresponding to this {@link TypeBased}
	 *         {@link FormComponent}.
	 */
	@Name(FORM_CONTEXT_PROVIDER)
	PolymorphicConfiguration<TypeBasedFormContextProvider> getFormContextProvider();
}

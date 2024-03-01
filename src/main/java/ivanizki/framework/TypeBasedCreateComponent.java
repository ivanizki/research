package ivanizki.framework;

import com.top_logic.basic.config.ConfigurationException;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.layout.form.component.FormComponent;
import com.top_logic.layout.form.model.FormContext;
import com.top_logic.model.TLClass;
import com.top_logic.model.util.TLModelUtil;

/**
 * A {@link TypeBased} {@link FormComponent}.
 *
 * @author ivanizki
 */
public class TypeBasedCreateComponent extends FormComponent implements TypeBased {

	/**
	 * Configuration for {@link TypeBasedCreateComponent}.
	 */
	public interface Config extends FormComponent.Config, TypeConfig, FormContextProviderConfig {
		// Fully determined by inherited configurations.
	}

	private TLClass _type;

	private TypeBasedFormContextProvider _formContextProvider;

	/**
	 * Creates a new {@link TypeBasedCreateComponent}.
	 */
	public TypeBasedCreateComponent(InstantiationContext context, Config config) throws ConfigurationException {
		super(context, config);
		String typeDescription = config.getType();
		_type = (TLClass) TLModelUtil.findType(typeDescription);
		_formContextProvider = context.getInstance(config.getFormContextProvider());
	}

	@Override
	public FormContext createFormContext() {
		return _formContextProvider.createFormContext(this);
	}

	@Override
	public TLClass getType() {
		return _type;
	}

}

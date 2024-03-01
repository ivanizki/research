package ivanizki.framework;

import com.top_logic.basic.CollectionUtil;
import com.top_logic.basic.config.ConfigurationException;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.layout.form.component.EditComponent;
import com.top_logic.layout.form.model.FormContext;
import com.top_logic.model.TLClass;
import com.top_logic.model.util.TLModelUtil;

/**
 * A {@link TypeBased} {@link EditComponent}.
 *
 * @author ivanizki
 */
public class TypeBasedEditComponent extends EditComponent implements TypeBased {

	/**
	 * Configuration for {@link TypeBasedEditComponent}.
	 */
	public interface Config extends EditComponent.Config, TypeConfig, FormContextProviderConfig {
		// Fully determined by inherited configurations.
	}

	private TLClass _type;

	private TypeBasedFormContextProvider _formContextProvider;

	/**
	 * Creates a new {@link TypeBasedEditComponent}.
	 */
	public TypeBasedEditComponent(InstantiationContext context, Config config) throws ConfigurationException {
		super(context, config);
		String typeDescription = CollectionUtil.getFirst(config.getDefaultFor());
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

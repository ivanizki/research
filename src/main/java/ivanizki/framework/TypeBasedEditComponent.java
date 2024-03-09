package ivanizki.framework;

import java.util.ArrayList;
import java.util.List;

import com.top_logic.basic.config.ConfigurationException;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.basic.config.misc.TypedConfigUtil;
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
		super(context, setDefaultFor(config));
		_type = getType(config);
		_formContextProvider = context.getInstance(config.getFormContextProvider());
	}

	private static Config setDefaultFor(Config config) {
		List<String> types = new ArrayList<>();
		for (TLClass subType : TLModelUtil.getConcreteSpecializations(getType(config))) {
			types.add(subType.toString());
		}
		return TypedConfigUtil.setProperty(config, Config.DEFAULT_FOR, types);
	}

	private static TLClass getType(Config config) {
		return (TLClass) TLModelUtil.findType(config.getType());
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

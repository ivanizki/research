package ivanizki.research.layout;

import java.util.ArrayList;
import java.util.List;

import com.top_logic.layout.table.model.NoDefaultColumnAdaption;
import com.top_logic.layout.table.model.TableConfiguration;

import ivanizki.research.model.Model;
import ivanizki.research.model.ModelType;

/**
 * {@link NoDefaultColumnAdaption} for the table of {@link ModelType#AUTHOR}s.
 *
 * @author ivanizki
 */
public class AuthorTableConfigurationProvider extends NoDefaultColumnAdaption {

	@Override
	public void adaptConfigurationTo(TableConfiguration table) {
		List<String> defaultColumns = new ArrayList<>();
		defaultColumns.add(Model.NAME);
		defaultColumns.add(Model.TOPICS);
		table.setDefaultColumns(defaultColumns);
	}

}

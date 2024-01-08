package ivanizki.research.layout;

import java.util.ArrayList;
import java.util.List;

import com.top_logic.basic.StringServices;
import com.top_logic.layout.IdentityAccessor;
import com.top_logic.layout.table.model.ColumnConfiguration;
import com.top_logic.layout.table.model.NoDefaultColumnAdaption;
import com.top_logic.layout.table.model.TableConfiguration;

import ivanizki.research.model.Model;
import ivanizki.research.model.ModelType;

/**
 * {@link NoDefaultColumnAdaption} for the table of {@link ModelType#MANUSCRIPT}s.
 *
 * @author ivanizki
 */
public class ManuscriptTableConfigurationProvider extends NoDefaultColumnAdaption {


	@Override
	public void adaptConfigurationTo(TableConfiguration table) {
		List<String> defaultColumns = new ArrayList<>();

		adaptYearColumn(table, defaultColumns);
		adaptAuthorsColumn(table, defaultColumns);
		adaptManuscriptColumn(table, defaultColumns);

		table.setDefaultColumns(defaultColumns);
	}

	private void adaptYearColumn(TableConfiguration table, List<String> defaultColumns) {
		ColumnConfiguration column = getDeclaredColumn(Model.YEAR, table, defaultColumns);
		column.setDefaultColumnWidth("70px");
	}

	private void adaptAuthorsColumn(TableConfiguration table, List<String> defaultColumns) {
		ColumnConfiguration column = getDeclaredColumn(Model.AUTHORS, table, defaultColumns);
		column.setDefaultColumnWidth("280px");
	}

	private void adaptManuscriptColumn(TableConfiguration table, List<String> defaultColumns) {
		ColumnConfiguration column = declareColumn(Model.MANUSCRIPT, table, defaultColumns);
		column.setDefaultColumnWidth("700px");
		column.setAccessor(IdentityAccessor.INSTANCE);
	}

	private ColumnConfiguration getDeclaredColumn(String columnName, TableConfiguration table, List<String> defaultColumns) {
		ColumnConfiguration column = table.getDeclaredColumn(columnName);
		defaultColumns.add(columnName);
		return column;
	}

	private ColumnConfiguration declareColumn(String columnName, TableConfiguration table, List<String> defaultColumns) {
		ColumnConfiguration column = table.declareColumn(columnName);
		column.setColumnLabel(StringServices.capitalizeString(columnName));
		defaultColumns.add(columnName);
		return column;
	}

}

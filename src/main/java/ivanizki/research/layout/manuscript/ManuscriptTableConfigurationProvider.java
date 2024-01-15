package ivanizki.research.layout.manuscript;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.top_logic.basic.AliasManager;
import com.top_logic.basic.StringServices;
import com.top_logic.basic.xml.TagWriter;
import com.top_logic.layout.DisplayContext;
import com.top_logic.layout.IdentityAccessor;
import com.top_logic.layout.basic.Command;
import com.top_logic.layout.basic.CommandModel;
import com.top_logic.layout.basic.CommandModelFactory;
import com.top_logic.layout.form.control.ButtonControl;
import com.top_logic.layout.form.control.ButtonRenderer;
import com.top_logic.layout.table.CellRenderer;
import com.top_logic.layout.table.TableRenderer.Cell;
import com.top_logic.layout.table.model.ColumnConfiguration;
import com.top_logic.layout.table.model.ColumnConfiguration.DisplayMode;
import com.top_logic.layout.table.model.NoDefaultColumnAdaption;
import com.top_logic.layout.table.model.TableConfiguration;
import com.top_logic.tool.boundsec.HandlerResult;
import com.top_logic.util.Resources;

import ivanizki.research.model.Model;
import ivanizki.research.model.ModelType;
import ivanizki.research.model.ModelUtil;

/**
 * {@link NoDefaultColumnAdaption} for the table of {@link ModelType#MANUSCRIPT}s.
 *
 * @author ivanizki
 */
public class ManuscriptTableConfigurationProvider extends NoDefaultColumnAdaption {

	@Override
	public void adaptConfigurationTo(TableConfiguration table) {
		List<String> defaultColumns = new ArrayList<>();

		adaptFilePathButtonColumn(table, defaultColumns);
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

	/**
	 * Column with buttons to copy the {@link Model#FILE_PATH} of {@link ModelType#MANUSCRIPT} to
	 * the {@link Clipboard}.
	 */
	private void adaptFilePathButtonColumn(TableConfiguration table, List<String> defaultColumns) {
		ColumnConfiguration column = declareColumn(Model.FILE_PATH + "Button", table, defaultColumns);
		column.setAccessor(IdentityAccessor.INSTANCE);
		column.setCellRenderer(new CellRenderer() {

			@Override
			public void writeCell(DisplayContext context, TagWriter out, Cell cell) throws IOException {
				String filePath = (String) ModelUtil.getValue(cell.getRowObject(), Model.FILE_PATH);
				if (!StringServices.isEmpty(filePath)) {
					ButtonControl button = createCopyButton(filePath);
					if (button != null) {
						button.write(context, out);
					}
				}
			}

			private ButtonControl createCopyButton(String filePath) {
				CommandModel commandModel = CommandModelFactory.commandModel(new Command() {
					@Override
					public HandlerResult executeCommand(DisplayContext context) {
						String libraryPath = AliasManager.getInstance().getAlias("%LIBRARY_PATH%");
						Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
						clipboard.setContents(new StringSelection(libraryPath + filePath), null);
						return HandlerResult.DEFAULT_RESULT;
					}
				});
				commandModel.setImage(Icons.CLIPBOARD);
				commandModel.setLabel(Resources.getInstance().getString(I18NConstants.COPY_FILE_PATH_TO_CLIPBOARD));
				return new ButtonControl(commandModel, ButtonRenderer.INSTANCE);
			}
		});
		column.setColumnLabelKey(I18NConstants.COPY_FILE_PATH_TO_CLIPBOARD);
		column.setDefaultColumnWidth("32px");
		column.setFilterProvider(null);
		column.setShowHeader(false);
		column.setSortable(false);
		column.setVisibility(DisplayMode.visible);
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

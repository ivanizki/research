package ivanizki.research.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.top_logic.basic.StringServices;
import com.top_logic.basic.col.MapUtil;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.element.model.DynamicModelService;
import com.top_logic.knowledge.service.PersistencyLayer;
import com.top_logic.knowledge.service.Transaction;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.DisplayContext;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.model.TLClass;
import com.top_logic.model.TLProperty;
import com.top_logic.model.TLStructuredTypePart;
import com.top_logic.model.impl.generated.TLModuleSingletonsBase;
import com.top_logic.tool.boundsec.AbstractCommandHandler;
import com.top_logic.tool.boundsec.HandlerResult;

import ivanizki.research.data.file.html.HTMLUtil;
import ivanizki.research.data.types.Composition;
import ivanizki.research.data.types.Data;
import ivanizki.research.data.types.Link;
import ivanizki.research.data.types.ListItem;
import ivanizki.research.data.types.Table;
import ivanizki.research.data.types.TableCell;
import ivanizki.research.data.types.TableRow;
import ivanizki.research.data.types.TextLine;
import ivanizki.research.data.types.UnorderedList;
import ivanizki.research.model.ModelUtil;
import ivanizki.research.model.Model;

/**
 * {@link AbstractCommandHandler} to import the entire database.
 *
 * @author ivanizki
 */
public class DataImportHandler extends AbstractCommandHandler {

	/**
	 * Creates a new {@link DataImportHandler}.
	 */
	public DataImportHandler(InstantiationContext context, Config config) {
		super(context, config);
	}

	@Override
	public HandlerResult handleCommand(DisplayContext context, LayoutComponent component, Object model, Map<String, Object> someArguments) {
		Data data = HTMLUtil.readFromHTML("tmp\\data.htm");
		Transaction transaction = PersistencyLayer.getKnowledgeBase().beginTransaction();
		try {
			new Importer().importData(data);
			transaction.commit();
		} finally {
			transaction.rollback();
		}
		return HandlerResult.DEFAULT_RESULT;
	}

	private static class Importer {

		private Map<String, TLClass> _types;

		private Map<TLClass, Wrapper> _singletons;

		private Map<TLClass, Table> _tables;

		private Map<String, Wrapper> _wrappers;

		public Importer() {
			_types = MapUtil.createValueMap(ModelUtil.getRelevantTypes(), type -> type.getName());
			_singletons = ModelUtil.getRelevantSingletons(_types.values());
		}

		private void importData(Data data) {
			_tables = indexTablesByType(data);
			_wrappers = indexWrappersByUid(_tables);
			for (TLClass type : _tables.keySet()) {
				importWrappers(type);
			}
		}

		private Map<TLClass, Table> indexTablesByType(Data data) {
			Map<TLClass, Table> tables = new HashMap<>();
			if (data instanceof UnorderedList) {
				for (ListItem item : ((UnorderedList) data).getItems()) {
					Data itemContent = item.getContent();
					if (itemContent instanceof Composition<?>) {
						List<?> itemParts = ((Composition<?>) itemContent).getParts();
						String typeName = ((TextLine) itemParts.get(0)).getString();
						TLClass type = _types.get(typeName);
						Table table = (Table) itemParts.get(1);
						tables.put(type, table);
					}
				}
			}
			return tables;
		}

		private Map<String, Wrapper> indexWrappersByUid(Map<TLClass, Table> tables) {
			Map<String, Wrapper> wrappers = new HashMap<>();
			for (TLClass type : tables.keySet()) {
				Table table = tables.get(type);
				for (TableRow row : table.getRows()) {
					if (!isHeader(row)) {
						TableCell firstCell = row.getCells().get(0);
						if (isUidCell(firstCell)) {
							String uid = ((Link) firstCell.getContent()).getUid();
							Wrapper wrapper = uid.endsWith(TLModuleSingletonsBase.SINGLETON_ATTR) ? _singletons.get(type)
									: (Wrapper) DynamicModelService.getInstance().createObject(type);
							wrappers.put(uid, wrapper);
						}
					}
				}
			}
			return wrappers;
		}

		private boolean isUidCell(TableCell cell) {
			Data content = cell.getContent();
			return content instanceof Link && !StringServices.isEmpty(((Link) content).getUid());
		}

		private void importWrappers(TLClass type) {
			List<TLStructuredTypePart> attributeNames = getAttributes(type);
			for (TableRow row : _tables.get(type).getRows()) {
				if (!isHeader(row)) {
					Wrapper wrapper = _wrappers.get(getUid(row));
					importWrapper(wrapper, attributeNames, row);
				}
			}
		}

		private List<TLStructuredTypePart> getAttributes(TLClass type) {
			List<TLStructuredTypePart> attributeNames = new ArrayList<>();
			Table table = _tables.get(type);
			TableRow header = table.getRows().get(0);
			for (TableCell cell : header.getCells()) {
				Data content = cell.getContent();
				if (content instanceof TextLine) {
					String attributeName = ((TextLine) content).getString();
					if (!Model.ID.equals(attributeName)) {
						attributeNames.add(type.getPart(attributeName));
					}
				}
			}
			return attributeNames;
		}

		private boolean isHeader(TableRow row) {
			Data firstCellContent = getFirstCellContent(row);
			return firstCellContent instanceof TextLine
				&& Model.ID.equals(((TextLine) firstCellContent).getString());
		}

		private void importWrapper(Wrapper wrapper, List<TLStructuredTypePart> attributes, TableRow row) {
			int columnIndex = 0;
			for (TableCell cell : row.getCells()) {
				if (columnIndex != 0) {
					TLStructuredTypePart attribute = attributes.get(columnIndex - 1);
					wrapper.setValue(attribute.getName(), importValue(attribute, cell));
				}
				columnIndex++;
			}
		}

		private Object importValue(TLStructuredTypePart attribute, TableCell cell) {
			if (attribute instanceof TLProperty) {
				return importPropertyValue(attribute, cell);
			}
			return importReferenceValue(attribute, cell);
		}

		private Object importPropertyValue(TLStructuredTypePart attribute, TableCell cell) {
			Data content = cell.getContent();
			if (content instanceof TextLine) {
				return ModelUtil.parse(attribute.getType(), ((TextLine) content).getString());
			}
			return null;
		}

		private Object importReferenceValue(TLStructuredTypePart attribute, TableCell cell) {
			Data content = cell.getContent();
			if (attribute.isMultiple()) {
				Collection<Wrapper> referredWrappers = attribute.isOrdered() ? new ArrayList<>() : new HashSet<>();
				if (content instanceof Link) {
					referredWrappers.add(resolveWrapper((Link) content));
				} else if (content instanceof Composition<?>) {
					for (Data part : ((Composition<?>) content).getParts()) {
						if (part instanceof Link) {
							referredWrappers.add(resolveWrapper((Link) part));
						}
					}
				}
				return referredWrappers;
			}
			if (content instanceof Link) {
				return resolveWrapper((Link) content);
			}
			return null;
		}

		private Wrapper resolveWrapper(Link link) {
			return _wrappers.get(link.getUid());
		}

		private String getUid(TableRow row) {
			return ((Link) getFirstCellContent(row)).getUid();
		}

		private Data getFirstCellContent(TableRow row) {
			return row.getCells().get(0).getContent();
		}
	}

}

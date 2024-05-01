package ivanizki.research.data;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.top_logic.basic.CollectionUtil;
import com.top_logic.basic.StringServices;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.element.meta.MetaElementUtil;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.DisplayContext;
import com.top_logic.layout.provider.MetaLabelProvider;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.model.TLClass;
import com.top_logic.model.TLProperty;
import com.top_logic.model.TLReference;
import com.top_logic.model.TLStructuredTypePart;
import com.top_logic.model.impl.generated.TLModuleSingletonsBase;
import com.top_logic.tool.boundsec.AbstractCommandHandler;
import com.top_logic.tool.boundsec.HandlerResult;
import com.top_logic.util.error.TopLogicException;

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
import ivanizki.research.model.Model;
import ivanizki.research.model.ModelUtil;

/**
 * {@link AbstractCommandHandler} to export the entire database.
 *
 * @author ivanizki
 */
public class DataExportHandler extends AbstractCommandHandler {

	/**
	 * Creates a new {@link DataExportHandler}.
	 */
	public DataExportHandler(InstantiationContext context, Config config) {
		super(context, config);
	}

	@Override
	public HandlerResult handleCommand(DisplayContext context, LayoutComponent component, Object model, Map<String, Object> someArguments) {
		try {
			HTMLUtil.writeToHTML("tmp\\data.htm", new RelevantDataCollector().getData());
		} catch (IOException e) {
			throw new TopLogicException(ivanizki.research.data.file.I18NConstants.FAILED_TO_WRITE_TO_FILE, e);
		}
		return HandlerResult.DEFAULT_RESULT;
	}

	private static class RelevantDataCollector {

		private static final boolean SINGLETON = true;

		private Set<TLClass> _types;

		private Map<TLClass, Wrapper> _singletons;

		public RelevantDataCollector() {
			_types = ModelUtil.getRelevantTypes();
			_singletons = ModelUtil.getRelevantSingletons(_types);
		}

		private Data getData() {
			UnorderedList data = new UnorderedList();
			for (TLClass type : _types) {
				Composition<Data> item = new Composition<>();
				item.add(new TextLine(type.getName()));
				Table table = new Table();
				List<TLStructuredTypePart> attributes = getRelevantAttributes(type);
				table.add(createHeader(attributes));
				List<Wrapper> nonSingletons = getInstances(type);
				Wrapper singleton = _singletons.get(type);
				if (singleton != null) {
					table.add(createRow(singleton, attributes, SINGLETON));
					nonSingletons.remove(singleton);
				}
				for (Wrapper wrapper : nonSingletons) {
					table.add(createRow(wrapper, attributes, !SINGLETON));
				}
				item.add(table);
				data.add(new ListItem(item));
			}
			return data;
		}

		private List<TLStructuredTypePart> getRelevantAttributes(TLClass type) {
			List<TLStructuredTypePart> attributes = ModelUtil.getOwnAttributes(type);
			attributes.removeIf(attribute -> !isRelevantAttribute(attribute));
			return attributes;
		}

		private boolean isRelevantAttribute(TLStructuredTypePart attribute) {
			if (attribute instanceof TLReference) {
				if (((TLReference) attribute).isDerived()) {
					return false;
				}
			}
			return true;
		}

		private TableRow createHeader(List<? extends TLStructuredTypePart> attributes) {
			TableRow row = new TableRow();
			row.add(new TableCell(new TextLine(Model.ID)));
			for (TLStructuredTypePart attribute : attributes) {
				row.add(new TableCell(new TextLine(attribute.getName())));
			}
			return row;
		}

		private List<Wrapper> getInstances(TLClass type) {
			return MetaElementUtil.getAllDirectInstancesOf(type, Wrapper.class);
		}

		private TableRow createRow(Wrapper wrapper, List<TLStructuredTypePart> attributes, boolean isSingleton) {
			TableRow row = new TableRow();
			row.add(createUidCell(wrapper, isSingleton));
			for (TLStructuredTypePart attribute : attributes) {
				row.add(createValueCell(wrapper.tValue(attribute), attribute));
			}
			return row;
		}

		private TableCell createUidCell(Wrapper wrapper, boolean isSingleton) {
			return new TableCell(new Link(getUid(wrapper, isSingleton)));
		}

		private String getUid(Wrapper wrapper, boolean isSingleton) {
			String uid = wrapper.tId().toString();
			return isSingleton ? uid + TLModuleSingletonsBase.SINGLETON_ATTR : uid;
		}

		private TableCell createValueCell(Object value, TLStructuredTypePart attribute) {
			if (attribute instanceof TLProperty) {
				return createPropertyValueCell(value);
			}
			return createReferenceValueCell(value);
		}

		private TableCell createPropertyValueCell(Object value) {
			return new TableCell(new TextLine(StringServices.toString(value)));
		}

		private TableCell createReferenceValueCell(Object value) {
			Composition<Link> links = new Composition<>();
			for (Wrapper referredWrapper : getReferredWrappers(value)) {
				links.add(new Link(getUid(referredWrapper, isSingleton(referredWrapper)), getLabel(referredWrapper)));
			}
			return new TableCell(links);
		}

		private List<Wrapper> getReferredWrappers(Object value) {
			return CollectionUtil.dynamicCastView(Wrapper.class, CollectionUtil.asList(value));
		}

		private boolean isSingleton(Wrapper wrapper) {
			return _singletons.containsValue(wrapper);
		}

		private String getLabel(Wrapper wrapper) {
			return MetaLabelProvider.INSTANCE.getLabel(wrapper);
		}
	}
}

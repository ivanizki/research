package ivanizki.research.data;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
import com.top_logic.model.util.TLModelUtil;
import com.top_logic.tool.boundsec.AbstractCommandHandler;
import com.top_logic.tool.boundsec.HandlerResult;

import ivanizki.research.DummyLogger;
import ivanizki.research.data.file.html.HTMLCompatible;
import ivanizki.research.data.types.Composition;
import ivanizki.research.data.types.Link;
import ivanizki.research.data.types.Table;
import ivanizki.research.data.types.TableCell;
import ivanizki.research.data.types.TableRow;
import ivanizki.research.data.types.Text;

/**
 * {@link AbstractCommandHandler} to export the entire database.
 *
 * @author ivanizki
 */
public class DataExportHandler extends AbstractCommandHandler {

	private static final String RELEVANT_MODULE_NAME = "research";

	private static final String RELEVANT_TYPE_NAME = "DataItem";

	/**
	 * Creates a new {@link DataExportHandler}.
	 */
	public DataExportHandler(InstantiationContext context, Config config) {
		super(context, config);
	}

	@Override
	public HandlerResult handleCommand(DisplayContext context, LayoutComponent component, Object model, Map<String, Object> someArguments) {
		Composition<HTMLCompatible> data = new Composition<>();
		for (TLClass type : getRelevantTypes()) {
			Table table = new Table();
			List<TLStructuredTypePart> attributes = getRelevantAttributes(type);
			table.add(createHeaderRow(attributes));
			for (Wrapper wrapper : MetaElementUtil.getAllDirectInstancesOf(type, Wrapper.class)) {
				TableRow row = new TableRow();
				row.add(new TableCell(new Link(wrapper.tId().toString())));
				for (TLStructuredTypePart attribute : attributes) {
					Object value = wrapper.tValue(attribute);
					if (attribute instanceof TLProperty) {
						row.add(new TableCell(new Text(StringServices.toString(value))));
					} else if (attribute instanceof TLReference) {
						Composition<Link> links = new Composition<>();
						for (Wrapper referredWrapper : CollectionUtil.dynamicCastView(Wrapper.class, CollectionUtil.asList(value))) {
							links.add(new Link(referredWrapper.tId().toString(), MetaLabelProvider.INSTANCE.getLabel(referredWrapper)));
						}
						row.add(new TableCell(links));
					} else {
						DummyLogger.error(attribute.tType().toString());
					}
				}
				table.add(row);
			}
			data.add(table);
		}
		try (FileWriter writer = new FileWriter("tmp\\data.htm", StandardCharsets.UTF_8)) {
			writer.write(data.toHTML());
			writer.close();
		} catch (IOException e) {
			DummyLogger.error(e);
		}
		return HandlerResult.DEFAULT_RESULT;
	}

	private List<TLStructuredTypePart> getRelevantAttributes(TLClass type) {
		List<TLStructuredTypePart> attributes = getOwnAttributes(type);
		attributes.removeIf(attribute -> !isRelevantAttribute(attribute));
		return attributes;
	}

	private List<TLStructuredTypePart> getOwnAttributes(TLClass type) {
		List<TLStructuredTypePart> attributes = new ArrayList<>();
		for (TLStructuredTypePart attribute : type.getAllParts()) {
			if (MetaElementUtil.isSubtype(RELEVANT_MODULE_NAME, RELEVANT_TYPE_NAME, (TLClass) attribute.getOwner())) {
				attributes.add(attribute);
			}
		}
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

	private TableRow createHeaderRow(List<? extends TLStructuredTypePart> attributes) {
		TableRow row = new TableRow();
		row.add(new TableCell(new Text("id")));
		for (TLStructuredTypePart attribute : attributes) {
			row.add(new TableCell(new Text(attribute.getName())));
		}
		return row;
	}

	private Set<TLClass> getRelevantTypes() {
		TLClass generalType = (TLClass) TLModelUtil.findType(RELEVANT_MODULE_NAME, RELEVANT_TYPE_NAME);
		return TLModelUtil.getConcreteSpecializations(generalType);
	}

}

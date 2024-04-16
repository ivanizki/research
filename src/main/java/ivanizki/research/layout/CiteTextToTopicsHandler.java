package ivanizki.research.layout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.top_logic.basic.CollectionUtil;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.element.layout.grid.GridComponent;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.DisplayContext;
import com.top_logic.layout.form.model.FormGroup;
import com.top_logic.layout.form.model.SelectField;
import com.top_logic.layout.form.model.StringField;
import com.top_logic.layout.form.model.TableField;
import com.top_logic.layout.table.TableViewModel;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.model.TLClass;
import com.top_logic.model.util.TLModelUtil;
import com.top_logic.tool.boundsec.AbstractCommandHandler;
import com.top_logic.tool.boundsec.HandlerResult;

import ivanizki.research.model.Model;
import ivanizki.research.model.ModelModule;
import ivanizki.research.model.ModelType;
import ivanizki.research.model.ModelUtil;

/**
 * {@link AbstractCommandHandler} that helps to find {@link ModelType#TOPIC}s in the
 * {@link Model#TEXT} of the edited {@link ModelType#CITE}.
 *
 * @author ivanizki
 */
public class CiteTextToTopicsHandler extends AbstractCommandHandler {

	private static final TLClass TOPIC_TYPE = (TLClass) TLModelUtil.findType(ModelModule.TOPICS, ModelType.TOPIC);

	/**
	 * Creates a new {@link CiteTextToTopicsHandler}.
	 */
	public CiteTextToTopicsHandler(InstantiationContext context, Config config) {
		super(context, config);
	}

	@Override
	public HandlerResult handleCommand(DisplayContext context, LayoutComponent component, Object model, Map<String, Object> someArguments) {
		GridComponent grid = (GridComponent) component;
		addTopics(getTopicsField(grid), getCiteText(grid));
		return HandlerResult.DEFAULT_RESULT;
	}

	private void addTopics(SelectField topicsField, String text) {
		Collection<Wrapper> topics = initTopics(topicsField);
		for (Wrapper topic : getAllTopics()) {
			if (text.contains(asText(topic))) {
				addTopic(topics, topic);
			}
		}
		topicsField.setValue(topics);
	}

	private void addTopic(Collection<Wrapper> topics, Wrapper topic) {
		if (!topics.contains(topic)) {
			topics.add(topic);
		}
	}

	private Collection<Wrapper> initTopics(SelectField topicsField) {
		return new ArrayList<>(CollectionUtil.dynamicCastView(Wrapper.class, (Collection<?>) topicsField.getValue()));
	}

	private List<Wrapper> getAllTopics() {
		return ModelUtil.getAllWrappers(TOPIC_TYPE);
	}

	private String asText(Wrapper topic) {
		return normalize((String) topic.getValue(Model.NAME));
	}

	private SelectField getTopicsField(GridComponent grid) {
		return (SelectField) getSelectedValueAt(grid, Model.TOPICS);
	}

	private String getCiteText(GridComponent grid) {
		StringField field = (StringField) getSelectedValueAt(grid, Model.TEXT);
		String text = normalize((String) field.getValue());
		text = text.replaceAll("\\s", " ");
		text = text.replaceAll("\\s\\s", " ");
		return text;
	}

	private Object getSelectedValueAt(GridComponent grid, String columnName) {
		return getViewModel(grid).getValueAt(getSelectedFormGroup(grid), columnName);
	}

	private TableViewModel getViewModel(GridComponent grid) {
		return ((TableField) grid.getFormContext().getMember(GridComponent.FIELD_TABLE)).getViewModel();
	}

	private FormGroup getSelectedFormGroup(GridComponent grid) {
		return CollectionUtil.getFirst(grid.getSelectedFormGroupsUnsorted());
	}

	private String normalize(String string) {
		return string.toLowerCase();
	}

}

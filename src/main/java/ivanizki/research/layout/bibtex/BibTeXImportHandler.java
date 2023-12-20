package ivanizki.research.layout.bibtex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.top_logic.basic.col.MapBuilder;
import com.top_logic.basic.col.MapUtil;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.element.meta.MetaElementUtil;
import com.top_logic.element.model.DynamicModelService;
import com.top_logic.knowledge.service.PersistencyLayer;
import com.top_logic.knowledge.service.Transaction;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.DisplayContext;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.model.TLClass;
import com.top_logic.model.TLStructuredTypePart;
import com.top_logic.model.util.TLModelUtil;
import com.top_logic.tool.boundsec.AbstractCommandHandler;
import com.top_logic.tool.boundsec.HandlerResult;

import ivanizki.research.DummyLogger;
import ivanizki.research.data.DataUtil;
import ivanizki.research.data.file.bibtex.BibTeX;
import ivanizki.research.data.file.bibtex.BibTeX.BibTeXEntryAttribute;
import ivanizki.research.data.file.bibtex.BibTeX.BibTeXEntryType;
import ivanizki.research.data.file.bibtex.BibTeXDocument;
import ivanizki.research.data.file.bibtex.BibTeXEntry;
import ivanizki.research.data.file.bibtex.BibTeXUtil;
import ivanizki.research.model.Manuscript;
import ivanizki.research.model.Model;

/**
 * {@link AbstractCommandHandler} for {@link BibTeX} import.
 *
 * @author ivanizki
 */
public class BibTeXImportHandler extends AbstractCommandHandler {

	private static final String ARTWORKS = "Artworks";

	private static final Map<BibTeXEntryType, TLClass> _TYPE_MAP = new MapBuilder<BibTeXEntryType, TLClass>()
		.put(BibTeXEntryType.ARTICLE, (TLClass) TLModelUtil.findType(ARTWORKS, "Article"))
		.put(BibTeXEntryType.BOOK, (TLClass) TLModelUtil.findType(ARTWORKS, "Book"))
		.toMap();

	private static final TLClass AUTHOR_TYPE = (TLClass) TLModelUtil.findType("Humans", "Author");

	/**
	 * Creates a new {@link BibTeXImportHandler}.
	 */
	public BibTeXImportHandler(InstantiationContext context, Config config) {
		super(context, config);
	}

	@Override
	public HandlerResult handleCommand(DisplayContext context, LayoutComponent component, Object model,	Map<String, Object> someArguments) {
		BibTeXDocument document = BibTeXUtil.readFromBibTeX(((BibTeXImportDialog) component).getData());
		Transaction transaction = PersistencyLayer.getKnowledgeBase().beginTransaction();
		try {
			new Importer().importDocument(document);
			transaction.commit();
		} finally {
			transaction.rollback();
		}
		component.closeDialog();
		return HandlerResult.DEFAULT_RESULT;
	}

	private static class Importer {

		private Map<String, Wrapper> _authors;

		public Importer() {
			_authors = indexAuthorsByName();
		}

		private Map<String, Wrapper> indexAuthorsByName() {
			return MapUtil.createValueMap(getAllAuthors(), author -> (String) author.getValue(Model.NAME));
		}

		private List<Wrapper> getAllAuthors() {
			return MetaElementUtil.getAllDirectInstancesOf(AUTHOR_TYPE, Wrapper.class);
		}

		private void importDocument(BibTeXDocument document) {
			for (BibTeXEntry entry : document.getEntries()) {
				importEntry(entry);
			}
		}

		private void importEntry(BibTeXEntry entry) {
			BibTeXEntryType entryType = entry.getType();
			TLClass type = _TYPE_MAP.get(entryType);
			if (type == null) {
				DummyLogger.error("Cannot import entry type \"" + entryType + "\".");
				return;
			}
			Wrapper wrapper = (Wrapper) DynamicModelService.getInstance().createObject(type);
			for (BibTeXEntryAttribute bibAttribute : entry.getAttributes()) {
				String attributeName = bibAttribute.name().toLowerCase();
				TLStructuredTypePart attribute = type.getPart(attributeName);
				String bibAttributeValue = entry.getAttributeValue(bibAttribute);
				if (BibTeXEntryAttribute.AUTHOR.equals(bibAttribute)) {
					wrapper.setValue(Manuscript.AUTHORS, importAttributeAuthor(bibAttributeValue));
				} else {
					if (attribute != null) {
						wrapper.setValue(attributeName, DataUtil.parse(attribute.getType(), bibAttributeValue));
					}
				}
			}
		}
		
		private List<Wrapper> importAttributeAuthor(String string) {
			List<Wrapper> authors = new ArrayList<>();
			for (String name : string.split(" and ")) {
				Wrapper author = _authors.get(name);
				if (author == null) {
					author = (Wrapper) DynamicModelService.getInstance().createObject(AUTHOR_TYPE);
					author.setValue(Model.NAME, name);
					_authors.put(name, author);
				}
				authors.add(author);
			}
			return authors;
		}

	}
	
}

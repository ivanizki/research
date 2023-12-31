package ivanizki.research.layout.bibtex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.top_logic.basic.col.MapBuilder;
import com.top_logic.basic.config.InstantiationContext;
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
import ivanizki.research.data.file.bibtex.BibTeX;
import ivanizki.research.data.file.bibtex.BibTeX.BibTeXEntryAttribute;
import ivanizki.research.data.file.bibtex.BibTeX.BibTeXEntryType;
import ivanizki.research.data.file.bibtex.BibTeXDocument;
import ivanizki.research.data.file.bibtex.BibTeXEntry;
import ivanizki.research.data.file.bibtex.BibTeXUtil;
import ivanizki.research.model.Model;
import ivanizki.research.model.ModelModule;
import ivanizki.research.model.ModelType;
import ivanizki.research.model.ModelUtil;

/**
 * {@link AbstractCommandHandler} for {@link BibTeX} import.
 *
 * @author ivanizki
 */
public class BibTeXImportHandler extends AbstractCommandHandler {

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

		private static final Map<BibTeXEntryType, TLClass> _TYPE_MAP = new MapBuilder<BibTeXEntryType, TLClass>()
			.put(BibTeXEntryType.ARTICLE, (TLClass) TLModelUtil.findType(ModelModule.ARTWORKS, ModelType.ARTICLE))
			.put(BibTeXEntryType.BOOK, (TLClass) TLModelUtil.findType(ModelModule.ARTWORKS, ModelType.BOOK))
			.toMap();

		private static final TLClass AUTHOR_TYPE = (TLClass) TLModelUtil.findType(ModelModule.HUMANS, ModelType.AUTHOR);
		
		private static final TLClass JOURNAL_TYPE = (TLClass) TLModelUtil.findType(ModelModule.HUMANS, ModelType.JOURNAL);

		private Map<String, Wrapper> _authors;

		private Map<String, Wrapper> _journals;

		public Importer() {
			_authors = ModelUtil.indexByAttribute(ModelUtil.getAllWrappers(AUTHOR_TYPE), Model.NAME);
			_journals = ModelUtil.indexByAttribute(ModelUtil.getAllWrappers(JOURNAL_TYPE), Model.NAME);
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
					wrapper.setValue(Model.AUTHORS, importAttributeAuthor(bibAttributeValue));
				} else if (BibTeXEntryAttribute.JOURNAL.equals(bibAttribute)) {
					wrapper.setValue(Model.JOURNAL, importAttributeJournal(bibAttributeValue));
				} else {
					if (attribute != null) {
						wrapper.setValue(attributeName, ModelUtil.parse(attribute.getType(), bibAttributeValue));
					}
				}
			}
		}
		
		private List<Wrapper> importAttributeAuthor(String names) {
			List<Wrapper> authors = new ArrayList<>();
			for (String name : names.split(" and ")) {
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

		private Wrapper importAttributeJournal(String name) {
			Wrapper journal = _journals.get(name);
			if (journal == null) {
				journal = (Wrapper) DynamicModelService.getInstance().createObject(JOURNAL_TYPE);
				journal.setValue(Model.NAME, name);
				_journals.put(name, journal);
			}
			return journal;
		}

	}
	
}

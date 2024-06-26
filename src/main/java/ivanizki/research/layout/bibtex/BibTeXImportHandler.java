package ivanizki.research.layout.bibtex;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.top_logic.basic.StringServices;
import com.top_logic.basic.col.MapBuilder;
import com.top_logic.basic.col.MapUtil;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.basic.io.binary.BinaryData;
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
import com.top_logic.util.error.TopLogicException;

import ivanizki.research.data.ASCII;
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
		BibTeXDocument document = readFromBibTeX(((BibTeXImportDialog) component).getData());
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

	/**
	 * Reads the {@link BibTeXDocument} from the specified {@link BinaryData}.
	 */
	public static BibTeXDocument readFromBibTeX(BinaryData data) {
		try {
			return BibTeXUtil.readFromBibTeX(data.getStream());
		} catch (IOException e) {
			throw new TopLogicException(ivanizki.research.data.file.I18NConstants.FAILED_TO_READ_FROM_FILE, e);
		}
	}

	private static class Importer {

		private static final Map<BibTeXEntryType, TLClass> TYPE_MAP = new MapBuilder<BibTeXEntryType, TLClass>()
			.put(BibTeXEntryType.ARTICLE, (TLClass) TLModelUtil.findType(ModelModule.ARTWORKS, ModelType.ARTICLE))
			.put(BibTeXEntryType.BOOK, (TLClass) TLModelUtil.findType(ModelModule.ARTWORKS, ModelType.BOOK))
			.put(BibTeXEntryType.PHDTHESIS, (TLClass) TLModelUtil.findType(ModelModule.ARTWORKS, ModelType.PH_D_THESIS))
			.toMap();

		private static final TLClass AUTHOR_TYPE = (TLClass) TLModelUtil.findType(ModelModule.HUMANS, ModelType.AUTHOR);
		
		private static final TLClass JOURNAL_TYPE = (TLClass) TLModelUtil.findType(ModelModule.HUMANS, ModelType.JOURNAL);

		private static final TLClass SCHOOL_TYPE = (TLClass) TLModelUtil.findType(ModelModule.HUMANS, ModelType.SCHOOL);

		private Map<String, Wrapper> _manuscripts;

		private Map<String, Wrapper> _authors;

		private Map<String, Wrapper> _journals;

		private Map<String, Wrapper> _schools;

		public Importer() {
			_manuscripts = indexByTitle(getAllManuscripts());
			_authors = ModelUtil.indexByAttribute(ModelUtil.getAllWrappers(AUTHOR_TYPE), Model.NAME);
			_journals = ModelUtil.indexByAttribute(ModelUtil.getAllWrappers(JOURNAL_TYPE), Model.NAME);
			_schools = ModelUtil.indexByAttribute(ModelUtil.getAllWrappers(SCHOOL_TYPE), Model.NAME);
		}

		public Map<String, Wrapper> indexByTitle(Collection<Wrapper> wrappers) {
			return MapUtil.createValueMap(wrappers, wrapper -> getKey((String) wrapper.getValue(Model.TITLE)));
		}

		private String getKey(String string) {
			if (string != null) {
				return string.toLowerCase();
			}
			return StringServices.EMPTY_STRING;
		}

		private List<Wrapper> getAllManuscripts() {
			List<Wrapper> manuscripts = new ArrayList<>();
			for (TLClass type : TYPE_MAP.values()) {
				manuscripts.addAll(ModelUtil.getAllWrappers(type));
			}
			return manuscripts;
		}

		private void importDocument(BibTeXDocument document) {
			for (BibTeXEntry entry : document.getEntries()) {
				importEntry(entry);
			}
		}

		private void importEntry(BibTeXEntry entry) {
			BibTeXEntryType entryType = entry.getType();
			TLClass type = TYPE_MAP.get(entryType);
			if (type == null) {
				throw new TopLogicException(I18NConstants.BIBTEX_IMPORT_UNSUPPORTED__ENTRY_TYPE.fill(entryType.toString()));
			}
			Wrapper wrapper = _manuscripts.get(getKey(entry.getAttributeValue(BibTeXEntryAttribute.TITLE)));
			wrapper = wrapper == null ? (Wrapper) DynamicModelService.getInstance().createObject(type) : wrapper;
			for (BibTeXEntryAttribute bibAttribute : entry.getAttributes()) {
				String attributeName = bibAttribute.name().toLowerCase();
				TLStructuredTypePart attribute = type.getPart(attributeName);
				String bibAttributeValue = entry.getAttributeValue(bibAttribute);
				if (BibTeXEntryAttribute.AUTHOR.equals(bibAttribute)) {
					wrapper.setValue(Model.AUTHORS, importAttributeAuthors(bibAttributeValue));
				} else if (BibTeXEntryAttribute.JOURNAL.equals(bibAttribute)) {
					wrapper.setValue(Model.JOURNAL, importAttributeJournal(bibAttributeValue));
				} else if (BibTeXEntryAttribute.SCHOOL.equals(bibAttribute)) {
					wrapper.setValue(Model.SCHOOL, importAttributeSchool(bibAttributeValue));
				} else {
					if (attribute != null) {
						wrapper.setValue(attributeName, ModelUtil.parse(attribute.getType(), bibAttributeValue));
					}
				}
			}
		}
		
		private List<Wrapper> importAttributeAuthors(String namesString) {
			List<Wrapper> authors = new ArrayList<>();
			for (String name : splitNames(namesString)) {
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

		private List<String> splitNames(String namesString) {
			List<String> names = new ArrayList<>();
			for (String namesSplit : namesString.split(BibTeX.LAST_AUTHOR_SEPARATOR)) {
				for (String name : namesSplit.split(Character.toString(ASCII.COMMA))) {
					names.add(name.trim());
				}
			}
			return names;
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

		private Wrapper importAttributeSchool(String name) {
			Wrapper school = _schools.get(name);
			if (school == null) {
				school = (Wrapper) DynamicModelService.getInstance().createObject(SCHOOL_TYPE);
				school.setValue(Model.NAME, name);
				_schools.put(name, school);
			}
			return school;
		}

	}
	
}

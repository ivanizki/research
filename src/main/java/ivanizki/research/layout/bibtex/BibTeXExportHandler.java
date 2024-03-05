package ivanizki.research.layout.bibtex;

import java.util.Collection;
import java.util.Map;

import com.top_logic.basic.CollectionUtil;
import com.top_logic.basic.StringServices;
import com.top_logic.basic.col.MapBuilder;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.basic.i18n.log.I18NLog;
import com.top_logic.basic.io.binary.BinaryData;
import com.top_logic.basic.util.ResKey;
import com.top_logic.knowledge.wrap.ValueProvider;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.table.component.TableComponent;
import com.top_logic.layout.table.export.AbstractTableExportHandler;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.model.TLClass;
import com.top_logic.model.TLStructuredType;
import com.top_logic.model.util.TLModelUtil;

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
 * {@link AbstractTableExportHandler} for {@link BibTeX} export.
 *
 * @author ivanizki
 */
public class BibTeXExportHandler extends AbstractTableExportHandler {

	private static final Map<TLClass, BibTeXEntryType> TYPE_MAP = new MapBuilder<TLClass, BibTeXEntryType>()
		.put((TLClass) TLModelUtil.findType(ModelModule.ARTWORKS, ModelType.ARTICLE), BibTeXEntryType.ARTICLE)
		.put((TLClass) TLModelUtil.findType(ModelModule.ARTWORKS, ModelType.BOOK), BibTeXEntryType.BOOK)
		.put((TLClass) TLModelUtil.findType(ModelModule.ARTWORKS, ModelType.PH_D_THESIS), BibTeXEntryType.PHDTHESIS)
		.toMap();

	/**
	 * Creates a new {@link BibTeXExportHandler}.
	 */
	public BibTeXExportHandler(InstantiationContext context, Config config) {
		super(context, config);
	}

	@Override
	protected ResKey getDefaultI18NKey() {
		return I18NConstants.BIBTEX_EXPORT_HANDLER_LABEL;
	}

	@Override
	protected BinaryData createDownloadData(I18NLog log, LayoutComponent component) {
		log.info(com.top_logic.layout.table.export.I18NConstants.STARTING_EXPORT);

		log.info(com.top_logic.layout.table.export.I18NConstants.EXTRACTING_TABLE_DATA);
		Collection<Wrapper> manuscripts = collectManuscripts(component);

		log.info(com.top_logic.layout.table.export.I18NConstants.EXPORTING_DATA);
		BibTeXDocument document = exportDocument(manuscripts);

		log.info(com.top_logic.layout.table.export.I18NConstants.PREPARING_DOWNLOAD);
		return BibTeXUtil.writeToBibTeX(document);
	}

	private Collection<Wrapper> collectManuscripts(LayoutComponent component) {
		return CollectionUtil.dynamicCastView(Wrapper.class, ((TableComponent) component).getSelectionModel().getSelection());
	}

	private BibTeXDocument exportDocument(Collection<Wrapper> manuscripts) {
		BibTeXDocument document = new BibTeXDocument();
		for (Wrapper manuscript : manuscripts) {
			BibTeXEntry entry = exportEntry(manuscript);
			if (entry != null) {
				document.add(entry);
			}
		}
		return document;
	}

	private BibTeXEntry exportEntry(Wrapper manuscript) {
		TLStructuredType type = manuscript.tType();
		BibTeXEntryType entryType = TYPE_MAP.get(type);
		if (entryType != null) {
			BibTeXEntry entry = new BibTeXEntry(entryType, getId(entryType, manuscript));
			entry.setAttributeValue(BibTeXEntryAttribute.TITLE, manuscript.getValue(Model.TITLE).toString());
			for (BibTeXEntryAttribute bibAttribute : BibTeXEntryAttribute.values()) {
				String attributeName = BibTeXEntryAttribute.AUTHOR.equals(bibAttribute) ? Model.AUTHORS
					: bibAttribute.name().toLowerCase();
				if (type.getPart(attributeName) != null) {
					if (BibTeXEntryAttribute.AUTHOR.equals(bibAttribute)) {
						entry.setAttributeValue(bibAttribute,
							exportAttributeAuthors(ModelUtil.getValueProviders(manuscript, attributeName)));
					} else if (BibTeXEntryAttribute.JOURNAL.equals(bibAttribute)) {
						entry.setAttributeValue(bibAttribute,
							exportAttributeJournal((ValueProvider) ModelUtil.getValue(manuscript, attributeName)));
					} else if (BibTeXEntryAttribute.SCHOOL.equals(bibAttribute)) {
						entry.setAttributeValue(bibAttribute,
							exportAttributeSchool((ValueProvider) ModelUtil.getValue(manuscript, attributeName)));
					} else {
						Object value = manuscript.getValue(attributeName);
						if (value != null) {
							entry.setAttributeValue(bibAttribute, value.toString());
						}
					}
				}
			}
			return entry;
		}
		return null;
	}

	private String getId(BibTeXEntryType entryType, Wrapper manuscript) {
		StringBuilder id = new StringBuilder(entryType.name().toLowerCase());
		id.append(ASCII.UNDERSCORE);
		for (ValueProvider author : ModelUtil.getValueProviders(manuscript, Model.AUTHORS)) {
			id.append(getFamilyName(author));
			id.append(ASCII.UNDERSCORE);
		}
		id.append(manuscript.getValue(Model.YEAR));
		return id.toString();
	}

	private String getFamilyName(ValueProvider author) {
		String name = (String) author.getValue(Model.NAME);
		String[] nameParts = StringServices.split(name, ASCII.SPACE);
		return nameParts[nameParts.length - 1];
	}

	private String exportAttributeAuthors(Collection<ValueProvider> authors) {
		StringBuilder names = new StringBuilder();
		int index = 0;
		for (ValueProvider author : authors) {
			names.append(author.getValue(Model.NAME));
			if (index < authors.size() - 1) {
				if (index < authors.size() - 2) {
					names.append(ASCII.COMMA);
					names.append(ASCII.SPACE);
				} else {
					names.append(BibTeX.LAST_AUTHOR_SEPARATOR);
				}
			}
			index++;
		}
		return names.toString();
	}

	private String exportAttributeJournal(ValueProvider journal) {
		if (journal != null) {
			return (String) journal.getValue(Model.NAME);
		}
		return StringServices.EMPTY_STRING;
	}
	
	private String exportAttributeSchool(ValueProvider school) {
		if (school != null) {
			return (String) school.getValue(Model.NAME);
		}
		return StringServices.EMPTY_STRING;
	}

}

package ivanizki.research.layout.bibtex;

import com.top_logic.basic.config.ConfigurationException;
import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.basic.io.binary.BinaryData;
import com.top_logic.knowledge.gui.layout.upload.NoNameCheck;
import com.top_logic.layout.form.component.FormComponent;
import com.top_logic.layout.form.model.DataField;
import com.top_logic.layout.form.model.FormContext;
import com.top_logic.layout.form.model.FormFactory;

import ivanizki.research.data.file.bibtex.BibTeX;

/**
 * {@link FormComponent} for {@link BibTeX} import.
 *
 * @author ivanizki
 */
public class BibTeXImportDialog extends FormComponent {

	/** {@link DataField} for {@link BibTeX} file. */
	private static final String BIBTEX_FILE = "bibtexFile";

	/**
	 * Creates a new {@link BibTeXImportDialog}.
	 */
	public BibTeXImportDialog(InstantiationContext context, Config config) throws ConfigurationException {
		super(context, config);
	}

	@Override
	public FormContext createFormContext() {
		FormContext formContext = new FormContext(this);

		DataField dataField = FormFactory.newDataField(BIBTEX_FILE, NoNameCheck.INSTANCE);
		dataField.setAcceptedTypes(BibTeX.FILE_EXTENSIONS);
		formContext.addMember(dataField);

		return formContext;
	}

	/**
	 * @return The uploaded data, or <code>null</code>, if the {@link #BIBTEX_FILE} does not contain
	 *         any data.
	 */
	public BinaryData getData() {
		return ((DataField) getFormContext().getMember(BIBTEX_FILE)).getDataItem();
	}

}

package ivanizki.research.layout.manuscript;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Map;

import com.top_logic.basic.config.InstantiationContext;
import com.top_logic.knowledge.wrap.Wrapper;
import com.top_logic.layout.DisplayContext;
import com.top_logic.mig.html.layout.LayoutComponent;
import com.top_logic.tool.boundsec.AbstractCommandHandler;
import com.top_logic.tool.boundsec.HandlerResult;

import ivanizki.research.model.Model;
import ivanizki.research.model.ModelType;

/**
 * {@link AbstractCommandHandler} to copy the {@link Model#FILE_PATH} of
 * {@link ModelType#MANUSCRIPT} to the {@link Clipboard}.
 *
 * @author ivanizki
 */
public class ManuscriptFilePathClipboardHandler extends AbstractCommandHandler {

	/**
	 * Creates a new {@link ManuscriptFilePathClipboardHandler}.
	 */
	public ManuscriptFilePathClipboardHandler(InstantiationContext context, Config config) {
		super(context, config);
	}

	@Override
	public HandlerResult handleCommand(DisplayContext context, LayoutComponent component, Object model, Map<String, Object> someArguments) {
		if (model instanceof Wrapper) {
			String filePath = (String) ((Wrapper) model).getValue(Model.FILE_PATH);
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(new StringSelection(filePath), null);
		}
		return HandlerResult.DEFAULT_RESULT;
	}

}

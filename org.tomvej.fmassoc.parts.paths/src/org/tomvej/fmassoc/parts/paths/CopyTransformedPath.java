package org.tomvej.fmassoc.parts.paths;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.Display;
import org.tomvej.fmassoc.core.communicate.ContextObjects;

/**
 * Copies transformed path into clipboard.
 * 
 * @author Tomáš Vejpustek
 */
public class CopyTransformedPath {
	private Clipboard clipboard;

	/**
	 * Initialize clipboard.
	 * 
	 * @param display
	 */
	@PostConstruct
	public void initializeClipboard(Display display) {
		clipboard = new Clipboard(display);
	}

	/**
	 * Copy selected path into clipboard.
	 */
	@Execute
	public void execute(@Named(ContextObjects.TRANSFORMED_PATH) String path) {
		clipboard.setContents(new Object[] { path }, new Transfer[] { TextTransfer.getInstance() });
	}

	/**
	 * Release clipboard system resource.
	 */
	@PreDestroy
	public void disposeClipboard() {
		clipboard.dispose();
	}
}

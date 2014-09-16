package org.tomvej.fmassoc.parts.paths;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.tomvej.fmassoc.core.widgets.multisort.MultisortDialog;

/**
 * Dialog for path table multisort.
 * 
 * @author Tomáš Vejpustek
 */
public class MultisortHandler {
	private final MultisortDialog dialog;

	/**
	 * Create dialog.
	 */
	@Inject
	public MultisortHandler(Shell parent) {
		dialog = new MultisortDialog(parent);
	}

	/**
	 * Show multisort dialog and apply changes (if any).
	 */
	@Execute
	public void execute(IEventBroker broker) {
		dialog.open();
		if (dialog.getReturnCode() == Dialog.OK) {
			broker.post(MultisortTopic.SORT, dialog.getSort());
		}
	}

	/**
	 * Listen to columns of path table change.
	 */
	@Inject
	public void columnsChanged(@Optional @UIEventTopic(MultisortTopic.COLUMNS) Collection<TableColumn> columns) {
		if (columns != null) {
			dialog.setColumns(columns);
		}
	}


}
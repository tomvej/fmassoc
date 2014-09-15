package org.tomvej.fmassoc.parts.paths;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.tomvej.fmassoc.core.widgets.multisort.MultisortDialog;

public class MultisortHandler {
	private final MultisortDialog dialog;

	@Inject
	public MultisortHandler(Shell parent) {
		dialog = new MultisortDialog(parent);
	}

	@Execute
	public void execute(IEventBroker broker) {
		dialog.open();
	}

	@Inject
	public void columnsChanged(@Optional @UIEventTopic(MultisortTopic.COLUMNS) Collection<TableColumn> columns) {

		dialog.setColumns(columns);
	}


}
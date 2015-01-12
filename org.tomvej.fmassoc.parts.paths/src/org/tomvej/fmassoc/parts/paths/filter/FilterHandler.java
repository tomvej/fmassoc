package org.tomvej.fmassoc.parts.paths.filter;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

public class FilterHandler {
	private final FilterDialog dialog;
	private MHandledItem handle;


	@Inject
	public FilterHandler(Shell parent) {
		dialog = new FilterDialog(parent);
	}

	@Execute
	public void execute(MHandledItem handle) {
		this.handle = handle;
		boolean prevState = !handle.isSelected();
		handle.setSelected(true);

		dialog.open();
		if (dialog.getReturnCode() == Dialog.OK) {
			// FIXME
		} else {
			handle.setSelected(prevState);
		}
	}

	private void setDefaultTooltip() {
		if (handle != null) {
			handle.setTooltip(handle.getPersistedState().getOrDefault("tooltip", "Filter"));
		}
	}
}

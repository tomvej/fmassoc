package org.tomvej.fmassoc.parts.paths.filter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class FilterDialog extends Dialog {

	public FilterDialog(Shell parent) {
		super(parent);
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Path Filter");
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
}

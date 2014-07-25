package org.tomvej.fmassoc.core.widgets.tablechooser;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.model.db.Table;

public class TableChooserDialog extends Dialog {
	private TableChooser chooser;
	private Table selected;

	public TableChooserDialog(Shell parent) {
		super(parent);
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Select Table");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		chooser = new TableChooser(parent);
		chooser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

		chooser.setTableListener(table -> getButton(IDialogConstants.OK_ID).setEnabled(table != null));
		return chooser;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		getButton(IDialogConstants.OK_ID).setEnabled(false);
	}

	@Override
	protected void okPressed() {
		selected = chooser.getSelection();
		super.okPressed();
	}

	public Table getSelectedTable() {
		return selected;
	}

}

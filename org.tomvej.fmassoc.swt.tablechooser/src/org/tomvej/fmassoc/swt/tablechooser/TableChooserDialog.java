package org.tomvej.fmassoc.swt.tablechooser;

import java.util.Collection;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Dialog for choosing a table. Based on {@link TableChooser}.
 * 
 * @author Tomáš Vejpustek
 */
public class TableChooserDialog extends Dialog {
	private TableChooser chooser;
	private Table selected;
	private Collection<Table> tables;
	private Collection<Object> filter;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 *            Parent shell.
	 */
	public TableChooserDialog(Shell parent) {
		super(parent);
	}

	/**
	 * Set tables to choose from.
	 */
	public void setTables(Collection<Table> tables) {
		this.tables = tables;
		if (chooser != null) {
			chooser.setTables(tables);
		}
	}

	/**
	 * Set table filter.
	 */
	public void setFilter(Collection<Object> tables) {
		this.filter = tables;
		if (chooser != null) {
			chooser.setFilter(tables);
		}
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

		if (tables != null) {
			chooser.setTables(tables);
		}
		chooser.setFilter(filter);

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

	/**
	 * Return selected table (only after OK has been pressed).
	 */
	public Table getSelectedTable() {
		return selected;
	}

}

package org.tomvej.fmassoc.core.widgets.multisort;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.tomvej.fmassoc.core.tables.SortEntry;

public class MultisortDialog extends Dialog {
	private MultiSorter sorter;
	private Collection<TableColumn> columns;

	public MultisortDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Multisort");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		sorter = new MultiSorter(parent);
		sorter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		if (columns != null) {
			sorter.setColumns(columns);
		}
		return sorter;
	}

	public void setColumns(Collection<TableColumn> columns) {
		this.columns = Validate.noNullElements(columns);
		if (sorter != null) {
			sorter.setColumns(columns);
		}
	}

	public List<SortEntry> getSort() {
		if (sorter == null) {
			return Collections.emptyList();
		}
		// FIXME
		return Collections.emptyList();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}
}

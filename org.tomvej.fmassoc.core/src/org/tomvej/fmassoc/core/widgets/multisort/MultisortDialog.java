package org.tomvej.fmassoc.core.widgets.multisort;

import java.util.ArrayList;
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

/**
 * Dialog handling multisort.
 * 
 * @author Tomáš Vejpustek
 */
public class MultisortDialog extends Dialog {
	private MultiSorter sorter;
	private Collection<TableColumn> columns;
	private List<SortEntry> result = Collections.emptyList();

	/**
	 * Initialize dialog.
	 */
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

	/**
	 * Specify columns which can be sorted.
	 */
	public void setColumns(Collection<TableColumn> columns) {
		this.columns = Validate.noNullElements(columns);
		if (sorter != null) {
			sorter.setColumns(columns);
		}
	}

	@Override
	protected void okPressed() {
		result = Collections.unmodifiableList(new ArrayList<>(sorter.getSort()));
		super.okPressed();
	}

	/**
	 * Get the sorting order specified by this dialog.
	 */
	public List<SortEntry> getSort() {
		return result;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}
}

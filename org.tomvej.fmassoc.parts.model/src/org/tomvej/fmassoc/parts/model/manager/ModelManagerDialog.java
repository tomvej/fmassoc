package org.tomvej.fmassoc.parts.model.manager;

import java.util.function.Consumer;

import javax.inject.Inject;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.core.tables.TableLayoutSupport;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.core.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.parts.model.core.ModelEntry;

public class ModelManagerDialog extends TitleAreaDialog {
	private TableViewer table;
	private Button addBtn, editBtn, removeBtn;

	@Inject
	public ModelManagerDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialog = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(dialog, SWT.NONE);
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		container.setLayout(new GridLayout(2, false));

		table = TableLayoutSupport.createTableViewer(container, SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER,
				GridDataFactory.fillDefaults().grab(true, true).span(1, 3).create());
		TableViewerColumn column = new TableViewerColumn(table, SWT.LEFT);
		column.setLabelProvider(new TextColumnLabelProvider<ModelEntry>(model -> model.getLabel()));
		TableLayoutSupport.create(table, 1, false, column);


		addBtn = createButton(container, "Add", e -> {});
		editBtn = createButton(container, "Edit", e -> {});
		removeBtn = createButton(container, "Remove", e -> {});
		return dialog;
	}

	private Button createButton(Composite parent, String title, Consumer<SelectionEvent> action) {
		Button result = new Button(parent, SWT.PUSH);
		result.setText(title);
		result.addSelectionListener(new SelectionWrapper(action));
		result.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		return result;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Data model management");
		setMessage("Add, remove and configure available data models.");
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Models");
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}


}

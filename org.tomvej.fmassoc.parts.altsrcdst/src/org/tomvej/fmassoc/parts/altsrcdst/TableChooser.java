package org.tomvej.fmassoc.parts.altsrcdst;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.altsrcdst.popup.TablePopup;


public class TableChooser extends Composite {
	private final Text input;

	private Table table;

	public TableChooser(Composite parent, TablePopup popup) {
		super(parent, SWT.BORDER);

		setLayout(new GridLayout(3, false));

		input = new Text(this, SWT.SINGLE | SWT.BORDER);
		popup.attach(input, () -> table, this::tableSet);
		input.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		Button rmBtn = new Button(this, SWT.PUSH);
		rmBtn.setText("X");
		rmBtn.addSelectionListener(new SelectionWrapper(e -> dispose()));
	}

	private void tableSet(Table table) {
		this.table = table;
		input.setText(table.getName());
	}

}

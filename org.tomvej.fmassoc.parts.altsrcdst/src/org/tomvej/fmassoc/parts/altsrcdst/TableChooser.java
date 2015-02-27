package org.tomvej.fmassoc.parts.altsrcdst;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.core.wrappers.FocusGainedWrapper;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.model.db.Table;


public class TableChooser extends Composite {
	private static final int TIMEOUT = 500;
	private final TablePopup popup;
	private final Text input;

	private Table table;

	public TableChooser(Composite parent, TablePopup popup) {
		super(parent, SWT.BORDER);
		this.popup = Validate.notNull(popup);

		setLayout(new GridLayout(3, false));

		input = new Text(this, SWT.SINGLE | SWT.BORDER);
		input.addFocusListener(new FocusGainedWrapper(this::focusGained));
		input.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		Button rmBtn = new Button(this, SWT.PUSH);
		rmBtn.setText("X");
		rmBtn.addSelectionListener(new SelectionWrapper(e -> dispose()));
	}

	private void focusGained(FocusEvent e) {
		if ((e.time & 0xFFFFFFFFL) - popup.getLastDeactivatedTime() < TIMEOUT) {
			// if focus is gained too early, relegate it to the parent
			forceFocus();
		} else {
			popup.open(input, this::tableSet);
		}
	}

	private void tableSet(Table table) {
		this.table = table;
		input.setText(table.getName());
	}

}

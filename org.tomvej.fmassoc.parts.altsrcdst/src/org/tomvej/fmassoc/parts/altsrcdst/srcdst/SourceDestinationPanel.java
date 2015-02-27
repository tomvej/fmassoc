package org.tomvej.fmassoc.parts.altsrcdst.srcdst;

import java.util.Collection;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.tomvej.fmassoc.model.db.Table;

public class SourceDestinationPanel extends Composite {


	public SourceDestinationPanel(Composite parent) {
		super(parent, SWT.BORDER);
		setLayout(new GridLayout(2, false));

		Button addBtn = new Button(this, SWT.PUSH);
		addBtn.setLayoutData(GridDataFactory.swtDefaults().align(SWT.BEGINNING, SWT.FILL).create());
		addBtn.setText("Add");

		Button clearBtn = new Button(this, SWT.PUSH);
		clearBtn.setLayoutData(GridDataFactory.swtDefaults().align(SWT.END, SWT.FILL).create());
		clearBtn.setText("Clear");

		// TODO Auto-generated constructor stub
	}

	public void setTables(Collection<Table> tables) {

	}

}

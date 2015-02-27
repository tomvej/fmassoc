package org.tomvej.fmassoc.parts.altsrcdst.srcdst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.tomvej.fmassoc.core.dnd.CompositeDnDSupport;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.altsrcdst.popup.TablePopup;

public class SourceDestinationPanel extends Composite {
	private final TablePopup popup;
	private final Composite chooserPanel;
	private final CompositeDnDSupport dnd;
	private final List<TableChooser> choosers = new ArrayList<>();

	public SourceDestinationPanel(Composite parent) {
		super(parent, SWT.BORDER);
		setLayout(new GridLayout(2, false));

		chooserPanel = new Composite(this, SWT.NONE);
		chooserPanel.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).span(2, 1).create());
		chooserPanel.setLayout(new GridLayout());

		dnd = new CompositeDnDSupport(chooserPanel);

		Button addBtn = new Button(this, SWT.PUSH);
		addBtn.setLayoutData(GridDataFactory.fillDefaults().create());
		addBtn.setText("Add");
		addBtn.addSelectionListener(new SelectionWrapper(e -> addChooser()));

		Button clearBtn = new Button(this, SWT.PUSH);
		clearBtn.setLayoutData(GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.FILL).create());
		clearBtn.setText("Clear");
		clearBtn.addSelectionListener(new SelectionWrapper(e -> clearChoosers()));

		popup = new TablePopup(getShell(), new Point(250, 350));
	}

	private void addChooser() {
		TableChooser newChooser = new TableChooser(chooserPanel, popup);
		choosers.add(newChooser);
		newChooser.addDnDSupport(dnd);
		// TODO add dispose listener

		layout();
	}


	private void clearChoosers() {
		choosers.forEach(p -> p.dispose());
		choosers.clear();

		// choosers for source an destination
		addChooser();
		addChooser();
	}

	public void setTables(Collection<Table> tables) {
		clearChoosers();
		popup.setTables(tables);
	}

}

package org.tomvej.fmassoc.parts.srcdst;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.tomvej.fmassoc.core.widgets.tablechooser.TableChooser;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.model.db.Table;

public class DestinationChooser extends Group {
	@Override
	protected void checkSubclass() {} // allow subclassing

	private final TableChooser tables;
	private final Button switcher;
	// note: visibility will be difficult
	private final Composite destinationComposite;
	private final GridData destinationData;
	private final Button addBtn, delBtn, upBtn, downBtn;

	private Consumer<List<Table>> listener;

	public DestinationChooser(Composite parent) {
		super(parent, SWT.SHADOW_ETCHED_OUT);
		setLayout(new GridLayout());
		GridDataFactory layout = GridDataFactory.fillDefaults();

		destinationComposite = new Composite(this, SWT.NONE);
		destinationData = layout.grab(true, false).create();
		destinationComposite.setLayoutData(destinationData);

		tables = new TableChooser(this);
		tables.setLayoutData(layout.grab(true, true).create());
		tables.setTableListener(this::tableSelected);

		switcher = new Button(this, SWT.CHECK);
		switcher.setLayoutData(layout.grab(false, false).create());
		switcher.setText("Multiple successive destination tables");
		switcher.setSelection(true);
		switcher.addSelectionListener(new SelectionWrapper(this::multiSwitched));

		destinationComposite.setLayout(new GridLayout(2, false));
		// FIXME -- should be correct viewer
		ListViewer view = new ListViewer(destinationComposite);
		view.getList().setLayoutData(layout.grab(true, true).span(1, 4).create());

		addBtn = createDestinationCompositeButton("Add");
		delBtn = createDestinationCompositeButton("Remove");
		upBtn = createDestinationCompositeButton("Up");
		downBtn = createDestinationCompositeButton("Down");
	}

	private Button createDestinationCompositeButton(String text) {
		Button result = new Button(destinationComposite, SWT.PUSH);
		result.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		result.setText(text);
		return result;
	}

	private void tableSelected(Table target) {
		boolean selected = target != null;
		addBtn.setEnabled(selected);
		if (!isMulti() && listener != null) {
			listener.accept(selected ? Collections.singletonList(target) : null);
		}
	}

	private void multiSwitched(SelectionEvent event) {
		/* layout concerns */
		destinationComposite.setVisible(isMulti());
		destinationData.exclude = !isMulti();
		layout();
		setText("Destination Table" + (isMulti() ? "s" : ""));

		/* data concerns */
		// FIXME
	}

	private boolean isMulti() {
		return switcher.getSelection();
	}

	public void setTableListener(Consumer<List<Table>> listener) {
		this.listener = listener;
	}
}

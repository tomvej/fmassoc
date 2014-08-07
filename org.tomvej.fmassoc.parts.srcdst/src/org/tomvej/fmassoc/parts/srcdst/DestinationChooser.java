package org.tomvej.fmassoc.parts.srcdst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.tomvej.fmassoc.core.tables.TableLayoutSupport;
import org.tomvej.fmassoc.core.widgets.tablechooser.TableChooser;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.core.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.model.db.Table;

public class DestinationChooser extends Group {
	@Override
	protected void checkSubclass() {} // allow subclassing

	private final TableChooser tables;
	private final IObservableList destinations;
	private final TableViewer dstTable;
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
		dstTable = TableLayoutSupport.createTableViewer(destinationComposite, SWT.BORDER | SWT.MULTI
				| SWT.FULL_SELECTION | SWT.V_SCROLL, layout.grab(true, true).span(1, 4).create());
		dstTable.addSelectionChangedListener(this::destinationSelected);

		TableViewerColumn column = new TableViewerColumn(dstTable, SWT.LEFT);
		column.getColumn().setText("Name");
		column.setLabelProvider(new TextColumnLabelProvider<Table>(
				table -> table.getName() + " (" + table.getImplName() + ")"));
		TableLayoutSupport.create(dstTable, 1, true, column);

		dstTable.setContentProvider(new ObservableListContentProvider());
		destinations = Properties.selfList(Table.class).observe(new ArrayList<>());
		dstTable.setInput(destinations);

		addBtn = createDestinationCompositeButton("Add", this::addTable);
		delBtn = createDestinationCompositeButton("Remove", this::removeTables);
		upBtn = createDestinationCompositeButton("Up", e -> {});
		downBtn = createDestinationCompositeButton("Down", e -> {});
	}

	private Button createDestinationCompositeButton(String text, Consumer<SelectionEvent> action) {
		Button result = new Button(destinationComposite, SWT.PUSH);
		result.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		result.setText(text);
		result.addSelectionListener(new SelectionWrapper(action));
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

	@SuppressWarnings("unchecked")
	private void refreshFilter() {
		tables.setFilter(destinations);
	}

	private void destinationSelected(SelectionChangedEvent event) {
		delBtn.setEnabled(!dstTable.getSelection().isEmpty());
	}

	private void addTable(SelectionEvent event) {
		destinations.add(tables.getSelection());
		refreshFilter();
	}

	private void removeTables(SelectionEvent event) {
		destinations.removeAll(((IStructuredSelection) dstTable.getSelection()).toList());
		refreshFilter();
	}

	public void setTableListener(Consumer<List<Table>> listener) {
		this.listener = listener;
	}

	public void setTables(Collection<Table> tables) {
		this.tables.setTables(tables);
	}

}

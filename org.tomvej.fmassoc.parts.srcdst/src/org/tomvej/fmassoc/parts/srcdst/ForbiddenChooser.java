package org.tomvej.fmassoc.parts.srcdst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.tomvej.fmassoc.core.tables.ColumnSortSupport;
import org.tomvej.fmassoc.core.tables.TableLayoutSupport;
import org.tomvej.fmassoc.core.widgets.tablechooser.TableChooser;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.core.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.model.db.Table;

public class ForbiddenChooser extends Group {
	@Override
	protected void checkSubclass() {} // allow subclassing

	private final CheckboxTableViewer forbiddenTable;
	private final IObservableList forbidden = Properties.selfList(Table.class).observe(new ArrayList<>());
	private TableChooser tables;
	private Button addBtn, rmBtn;

	private Consumer<Set<Table>> listener;

	public ForbiddenChooser(Composite parent) {
		super(parent, SWT.SHADOW_ETCHED_OUT);
		setText("Forbidden tables");
		setLayout(new GridLayout(1, false));
		GridDataFactory layout = GridDataFactory.fillDefaults().grab(true, true);

		Composite upper = new Composite(this, SWT.NONE);
		upper.setLayoutData(layout.create());
		upper.setLayout(new GridLayout(2, true));

		forbiddenTable = TableLayoutSupport.createCheckboxTableViewer(upper, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.BORDER
				| SWT.MULTI, GridDataFactory.fillDefaults().grab(true, true).span(2, 1).create());
		forbiddenTable.getTable().setLinesVisible(true);
		forbiddenTable.getTable().setHeaderVisible(true);

		forbiddenTable.setContentProvider(new ObservableListContentProvider());
		forbiddenTable.setInput(forbidden);

		TableViewerColumn nameColumn = new TableViewerColumn(forbiddenTable, SWT.LEFT);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new TextColumnLabelProvider<Table>(t -> t.getName()));

		TableViewerColumn implNameColumn = new TableViewerColumn(forbiddenTable, SWT.LEFT);
		implNameColumn.getColumn().setText("Implementation name");
		implNameColumn.setLabelProvider(new TextColumnLabelProvider<Table>(t -> t.getImplName()));

		new ColumnSortSupport(forbiddenTable);

		TableViewerColumn checkColumn = new TableViewerColumn(forbiddenTable, SWT.CENTER, 0);
		checkColumn.setLabelProvider(new TextColumnLabelProvider<Table>(t -> ""));
		checkColumn.getColumn().setResizable(false);

		TableLayoutSupport.create(forbiddenTable, 1, true, nameColumn, implNameColumn).
				setupWidthColumn(checkColumn, 30, false, false);

		addBtn = createButton(upper, "Add", e -> addTable());
		rmBtn = createButton(upper, "Remove", e -> rmTable());

		tables = new TableChooser(this);
		tables.setLayoutData(layout.create());

		tables.setTableListener(t -> addBtn.setEnabled(t != null));
		forbiddenTable.addSelectionChangedListener(e -> rmBtn.setEnabled(!forbiddenTable.getSelection().isEmpty()));
		forbiddenTable.addCheckStateListener(e -> fireChanges());
	}

	private Button createButton(Composite parent, String title, Consumer<SelectionEvent> action) {
		Button result = new Button(parent, SWT.PUSH);
		result.setText(title);
		result.addSelectionListener(new SelectionWrapper(action));
		result.setLayoutData(GridDataFactory.fillDefaults().create());
		result.setEnabled(false);
		return result;
	}

	@SuppressWarnings("unchecked")
	private void refreshFilter() {
		tables.setFilter(forbidden);
	}

	private void addTable() {
		Table selected = tables.getSelection();
		forbidden.add(selected);
		forbiddenTable.setChecked(selected, true);
		refreshFilter();
		fireChanges();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void rmTable() {
		List selected = ((IStructuredSelection) forbiddenTable.getSelection()).toList();
		boolean changed = selected.stream().anyMatch(o -> forbiddenTable.getChecked(o));
		forbidden.removeAll(selected);
		refreshFilter();
		if (changed) {
			fireChanges();
		}
	}

	public void setTables(Collection<Table> tables) {
		this.tables.setTables(tables);
		forbidden.clear();
	}

	private void fireChanges() {
		if (listener != null) {
			listener.accept(getForbiddenTables());
		}
	}

	public void setTableListener(Consumer<Set<Table>> listener) {
		this.listener = listener;
	}

	@SuppressWarnings("unchecked")
	public Set<Table> getForbiddenTables() {
		return Collections.unmodifiableSet((Set<Table>)
				forbidden.stream().filter(t -> forbiddenTable.getChecked(t)).collect(Collectors.toSet()));
	}

}

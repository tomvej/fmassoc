package org.tomvej.fmassoc.core.widgets.multisort;

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
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.tomvej.fmassoc.core.tables.SortEntry;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.core.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.core.wrappers.TextLabelProvider;

public class MultiSorter extends Composite {
	private final ListViewer availableList;
	private final TableViewer selectedList;
	private final IObservableList available, selected;
	private final Button addBtn, rmBtn, addAllBtn, rmAllBtn, upBtn, downBtn;

	private Consumer<List<SortEntry>> listener;

	public MultiSorter(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(4, false));

		GridDataFactory listLayout = GridDataFactory.fillDefaults().span(1, 4).grab(true, true);
		GridDataFactory btnLayout = GridDataFactory.swtDefaults().grab(false, true);

		availableList = new ListViewer(this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		availableList.getControl().setLayoutData(listLayout.create());
		addAllBtn = createButton(">>", btnLayout.align(SWT.FILL, SWT.BOTTOM).create(), e -> addAll());
		selectedList = new TableViewer(this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		selectedList.getControl().setLayoutData(listLayout.create());
		new Label(this, SWT.NONE);
		addBtn = createButton(">", e -> {});
		upBtn = createButton("Up", e -> {});
		rmBtn = createButton("<", e -> {});
		downBtn = createButton("Down", e -> {});
		rmAllBtn = createButton("<<", btnLayout.align(SWT.FILL, SWT.TOP).create(), e -> rmAll());

		availableList.setLabelProvider(new TextLabelProvider<TableColumn>(c -> c.getText()));
		TableViewerColumn nameClmn = new TableViewerColumn(selectedList, SWT.LEFT);
		nameClmn.setLabelProvider(new TextColumnLabelProvider<SortEntry>(e -> e.getColumn().getText()));
		TableViewerColumn ascClmn = new TableViewerColumn(selectedList, SWT.LEFT);
		ascClmn.setLabelProvider(new TextColumnLabelProvider<SortEntry>(e -> e.isAscending() ? "Ascending" : "Descending"));
		nameClmn.getColumn().setWidth(50);
		ascClmn.getColumn().setWidth(25);

		available = Properties.selfList(TableColumn.class).observe(new ArrayList<>());
		availableList.setContentProvider(new ObservableListContentProvider());
		availableList.setInput(available);
		selected = Properties.selfList(SortEntry.class).observe(new ArrayList<>());
		selectedList.setContentProvider(new ObservableListContentProvider());
		selectedList.setInput(selected);

		availableList.addSelectionChangedListener(e -> this.refreshButtons());
		selectedList.addSelectionChangedListener(e -> this.refreshButtons());
	}

	private Button createButton(String title, Consumer<SelectionEvent> listener) {
		return createButton(title, GridDataFactory.fillDefaults().create(), listener);
	}

	private Button createButton(String title, GridData layoutData, Consumer<SelectionEvent> listener) {
		Button result = new Button(this, SWT.PUSH);
		result.setLayoutData(layoutData);
		result.setText(title);
		result.addSelectionListener(new SelectionWrapper(listener));
		return result;
	}

	private void selectedSelectionChanged(SelectionChangedEvent event) {
		rmBtn.setEnabled(event.getSelection().isEmpty());
		boolean single = ((IStructuredSelection) event.getSelection()).size() == 1;
		upBtn.setEnabled(single);
		downBtn.setEnabled(single);
	}

	private void refreshButtons() {
		addAllBtn.setEnabled(!available.isEmpty());
		rmAllBtn.setEnabled(!selected.isEmpty());

		addBtn.setEnabled(!availableList.getSelection().isEmpty());
		rmBtn.setEnabled(!selectedList.getSelection().isEmpty());

		boolean single = selectedList.getTable().getSelectionCount() == 1;
		int index = selectedList.getTable().getSelectionIndex();
		upBtn.setEnabled(single && index != 0);
		downBtn.setEnabled(single && index != selected.size() - 1);
	}

	@SuppressWarnings("unchecked")
	private List<TableColumn> available() {
		return available;
	}

	@SuppressWarnings("unchecked")
	private List<SortEntry> selected() {
		return selected;
	}

	@SuppressWarnings("unchecked")
	private List<TableColumn> availableSelection() {
		return ((IStructuredSelection) availableList.getSelection()).toList();
	}

	@SuppressWarnings("unchecked")
	private List<SortEntry> selectedSelection() {
		return ((IStructuredSelection) selectedList.getSelection()).toList();
	}

	private void addAll() {
		available().stream().map(c -> new SimpleSortEntry(c)).forEach(e -> selected.add(e));
		available.clear();
		refreshButtons();
	}

	private void rmAll() {
		selected().stream().map(e -> e.getColumn()).forEach(e -> available.add(e));
		selected.clear();
		refreshButtons();
	}

	public void setColumns(Collection<TableColumn> columns) {
		available.clear();
		available.addAll(columns);
		selected.clear();
		fireChanges();
		refreshButtons();
	}

	public List<SortEntry> getSort() {
		// FIXME
		return Collections.emptyList();
	}

	public void setSortListener(Consumer<List<SortEntry>> listener) {
		this.listener = listener;
	}

	private void fireChanges() {
		if (listener != null) {
			listener.accept(getSort());
		}
	}


}

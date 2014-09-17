package org.tomvej.fmassoc.core.widgets.multisort;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
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

/**
 * Component used to set set multisort for a table.
 * 
 * @author Tomáš Vejpustek
 */
public class MultiSorter extends Composite {
	private final ListViewer availableList;
	private final TableViewer selectedList;
	private final IObservableList available, selected;
	private final Button addBtn, rmBtn, addAllBtn, rmAllBtn, upBtn, downBtn;

	private Consumer<List<SortEntry>> listener;

	/**
	 * Create the component.
	 */
	public MultiSorter(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(4, false));

		GridDataFactory listLayout = GridDataFactory.fillDefaults().span(1, 4).grab(true, true);
		GridDataFactory btnLayout = GridDataFactory.swtDefaults().grab(false, true);

		availableList = new ListViewer(this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		availableList.getControl().setLayoutData(listLayout.create());
		addAllBtn = createButton(">>", btnLayout.align(SWT.FILL, SWT.BOTTOM).create(), e -> add(available()));
		selectedList = new TableViewer(this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.FULL_SELECTION);
		selectedList.getControl().setLayoutData(listLayout.create());
		new Label(this, SWT.NONE);
		addBtn = createButton(">", e -> add(availableSelection()));
		upBtn = createButton("Up", e -> swap(-1));
		rmBtn = createButton("<", e -> remove(selectedSelection()));
		downBtn = createButton("Down", e -> swap(1));
		rmAllBtn = createButton("<<", btnLayout.align(SWT.FILL, SWT.TOP).create(), e -> remove(selected()));

		availableList.setLabelProvider(new TextLabelProvider<TableColumn>(c -> c.getText()));
		TableViewerColumn nameClmn = new TableViewerColumn(selectedList, SWT.LEFT);
		nameClmn.setLabelProvider(new TextColumnLabelProvider<SortEntry>(e -> e.getColumn().getText()));
		TableViewerColumn ascClmn = new TableViewerColumn(selectedList, SWT.LEFT);
		ascClmn.setLabelProvider(new TextColumnLabelProvider<SortEntry>(e -> e.isAscending() ? "Ascending" : "Descending"));
		ascClmn.setEditingSupport(new AscendingEditingSupport(selectedList));
		nameClmn.getColumn().setWidth(100); // FIXME
		ascClmn.getColumn().setWidth(50); // FIXME

		available = Properties.selfList(TableColumn.class).observe(new ArrayList<>());
		availableList.setContentProvider(new ObservableListContentProvider());
		availableList.setInput(available);
		selected = Properties.selfList(SimpleSortEntry.class).observe(new ArrayList<>());
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

	private void add(List<TableColumn> columns) {
		columns.stream().map(c -> new SimpleSortEntry(c)).forEach(selected::add);
		available.removeAll(columns);
		fireChanges();
		refreshButtons();
	}

	private void remove(List<SortEntry> entries) {
		entries.stream().map(e -> e.getColumn()).forEach(available::add);
		selected.removeAll(entries);
		fireChanges();
		refreshButtons();
	}

	private void swap(int delta) {
		int index = selectedList.getTable().getSelectionIndex();
		selected.set(index, selected.set(index + delta, selected.get(index)));
		fireChanges();

		selectedList.getTable().setSelection(index + delta);
		fireChanges();
		refreshButtons();
	}

	/**
	 * Specify columns used in multisort definition.
	 */
	public void setColumns(Collection<TableColumn> columns) {
		available.clear();
		available.addAll(columns);
		selected.clear();
		fireChanges();
		refreshButtons();
	}

	/**
	 * Return currently chosen multisort.
	 */
	public List<SortEntry> getSort() {
		return selected();
	}

	void setSort(List<SortEntry> sort) {
		remove(selected());
		for (SortEntry entry : sort) {
			available.remove(entry.getColumn());
			SimpleSortEntry result = new SimpleSortEntry(entry.getColumn());
			result.setAscending(entry.isAscending());
			selected.add(result);
		}
		fireChanges();
	}

	/**
	 * Attach a listener which is notified on changes in sort.
	 */
	public void setSortListener(Consumer<List<SortEntry>> listener) {
		this.listener = listener;
	}

	private void fireChanges() {
		if (listener != null) {
			listener.accept(getSort());
		}
	}


}

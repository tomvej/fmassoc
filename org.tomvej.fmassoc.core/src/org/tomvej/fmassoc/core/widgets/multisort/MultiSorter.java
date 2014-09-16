package org.tomvej.fmassoc.core.widgets.multisort;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TableColumn;
import org.tomvej.fmassoc.core.tables.SortEntry;
import org.tomvej.fmassoc.core.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.core.wrappers.TextLabelProvider;

public class MultiSorter extends Composite {
	private final ListViewer availableList;
	private final TableViewer selectedList;
	private final Button addBtn, rmBtn, addAllBtn, rmAllBtn, upBtn, downBtn;

	private Consumer<List<SortEntry>> listener;

	public MultiSorter(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(4, false));

		GridDataFactory listLayout = GridDataFactory.fillDefaults().span(1, 4).grab(true, true);
		GridDataFactory btnLayout = GridDataFactory.swtDefaults().grab(false, true);

		availableList = new ListViewer(this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		availableList.getControl().setLayoutData(listLayout.create());
		addAllBtn = createButton(">>", btnLayout.align(SWT.FILL, SWT.BOTTOM).create());
		selectedList = new TableViewer(this, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		selectedList.getControl().setLayoutData(listLayout.create());
		new Label(this, SWT.NONE);
		addBtn = createButton(">");
		upBtn = createButton("Up");
		rmBtn = createButton("<");
		downBtn = createButton("Down");
		rmAllBtn = createButton(">>", btnLayout.align(SWT.FILL, SWT.TOP).create());

		availableList.setLabelProvider(new TextLabelProvider<TableColumn>(c -> c.getText()));
		TableViewerColumn nameClmn = new TableViewerColumn(selectedList, SWT.LEFT);
		nameClmn.setLabelProvider(new TextColumnLabelProvider<SortEntry>(e -> e.getColumn().getText()));
		TableViewerColumn ascClmn = new TableViewerColumn(selectedList, SWT.LEFT);
		ascClmn.setLabelProvider(new TextColumnLabelProvider<SortEntry>(e -> e.isAscending() ? "Ascending" : "Descending"));

		availableList.addSelectionChangedListener(e -> addBtn.setEnabled(!e.getSelection().isEmpty()));
		selectedList.addSelectionChangedListener(this::selectedSelectionChanged);
	}

	private Button createButton(String title) {
		return createButton(title, GridDataFactory.fillDefaults().create());
	}

	private Button createButton(String title, GridData layoutData) {
		Button result = new Button(this, SWT.PUSH);
		result.setLayoutData(layoutData);
		result.setText(title);
		return result;
	}

	private void selectedSelectionChanged(SelectionChangedEvent event) {
		rmBtn.setEnabled(event.getSelection().isEmpty());
		boolean single = ((IStructuredSelection) event.getSelection()).size() == 1;
		upBtn.setEnabled(single);
		downBtn.setEnabled(single);
	}

	public void setColumns(Collection<TableColumn> column) {
		availableList.getList().removeAll();
		availableList.add(column.toArray());
		// FIXME
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

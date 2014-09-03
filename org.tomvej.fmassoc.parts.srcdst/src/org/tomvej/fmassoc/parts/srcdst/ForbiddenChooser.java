package org.tomvej.fmassoc.parts.srcdst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.tomvej.fmassoc.core.tables.TableLayoutSupport;
import org.tomvej.fmassoc.core.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.model.db.Table;

public class ForbiddenChooser extends Group {
	@Override
	protected void checkSubclass() {} // allow subclassing

	private final CheckboxTableViewer forbiddenTable;
	private final IObservableList forbidden = Properties.selfList(Table.class).observe(new ArrayList<>());

	private Consumer<Set<Table>> listener;

	public ForbiddenChooser(Composite parent) {
		super(parent, SWT.SHADOW_ETCHED_OUT);
		setText("Forbidden tables");
		setLayout(new GridLayout());

		forbiddenTable = TableLayoutSupport.createCheckboxTableViewer(this, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.BORDER
				| SWT.MULTI, GridDataFactory.fillDefaults().grab(true, true).create());
		forbiddenTable.getTable().setLinesVisible(true);
		forbiddenTable.getTable().setHeaderVisible(true);

		forbiddenTable.setContentProvider(new ObservableListContentProvider());
		forbiddenTable.setInput(forbidden);

		TableViewerColumn checkColumn = new TableViewerColumn(forbiddenTable, SWT.CENTER);
		checkColumn.setLabelProvider(new TextColumnLabelProvider<Table>(t -> ""));
		checkColumn.getColumn().setResizable(false);

		TableViewerColumn nameColumn = new TableViewerColumn(forbiddenTable, SWT.LEFT);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new TextColumnLabelProvider<Table>(t -> t.getName()));

		TableViewerColumn implNameColumn = new TableViewerColumn(forbiddenTable, SWT.LEFT);
		implNameColumn.getColumn().setText("Implementation name");
		implNameColumn.setLabelProvider(new TextColumnLabelProvider<Table>(t -> t.getImplName()));

		TableLayoutSupport.create(forbiddenTable, 1, true, nameColumn, implNameColumn).
				setupWidthColumn(checkColumn, 30, false, false);
	}

	public void setTables(Collection<Table> tables) {

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

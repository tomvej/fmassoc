package org.tomvej.fmassoc.parts.altsrcdst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.tomvej.fmassoc.core.tables.ColumnSortSupport;
import org.tomvej.fmassoc.core.tables.TableLayoutSupport;
import org.tomvej.fmassoc.core.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.model.db.Table;

public class ForbiddenChooser extends Composite {
	private final CheckboxTableViewer table;
	private final IObservableList forbidden = Properties.selfList(Table.class).observe(new ArrayList<>());

	private Collection<Table> defaultForbidden = Collections.emptySet();
	private Consumer<Set<Table>> tableListener;

	public ForbiddenChooser(Composite parent) {
		super(parent, SWT.BORDER);
		setLayout(new GridLayout());

		table = TableLayoutSupport.createCheckboxTableViewer(this,
				SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.BORDER | SWT.MULTI,
				GridDataFactory.fillDefaults().grab(true, true).create());
		table.getTable().setHeaderVisible(true);
		table.getTable().setLinesVisible(true);
		createColumns();

		table.setContentProvider(new ObservableListContentProvider());
		table.setInput(forbidden);

		table.addCheckStateListener(e -> fireChanges());
	}

	private void createColumns() {
		/* name */
		TableViewerColumn nameColumn = new TableViewerColumn(table, SWT.LEFT);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new LabelProvider(t -> t.getName()));

		/* implementation name */
		TableViewerColumn implNameColumn = new TableViewerColumn(table, SWT.LEFT);
		implNameColumn.getColumn().setText("Implementation Name");
		implNameColumn.setLabelProvider(new LabelProvider(t -> t.getImplName()));

		new ColumnSortSupport(table);

		/* check */
		TableViewerColumn checkColumn = new TableViewerColumn(table, SWT.CENTER, 0);
		checkColumn.setLabelProvider(new TextColumnLabelProvider<Table>(t -> ""));
		checkColumn.getColumn().setResizable(false);

		TableLayoutSupport.create(table, 1, true, nameColumn, implNameColumn)
				.setupWidthColumn(checkColumn, 30, false, false);
	}

	public void setTables(Collection<Table> tables, Collection<Table> forbidden) {
		defaultForbidden = forbidden != null ? forbidden : Collections.emptySet();
		this.forbidden.clear();
		this.forbidden.addAll(defaultForbidden);
		table.setCheckedElements(defaultForbidden.toArray());
	}

	public void setTableListener(Consumer<Set<Table>> listener) {
		tableListener = listener;
	}

	private void fireChanges() {
		if (tableListener != null) {
			tableListener.accept(getForbiddenTables());
		}
	}

	@SuppressWarnings("unchecked")
	private Set<Table> getForbiddenTables() {
		return (Set<Table>) forbidden.stream().filter(t -> table.getChecked(t)).collect(Collectors.toSet());
	}


	/**
	 * Changes color for default forbidden tables.
	 * 
	 * @author Tomáš Vejpustek
	 */
	private class LabelProvider extends TextColumnLabelProvider<Table> {

		private LabelProvider(Function<Table, String> provider) {
			super(provider);
		}

		@Override
		public Color getForeground(Object element) {
			if (defaultForbidden.contains(element)) {
				return getShell().getDisplay().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
			}
			return super.getForeground(element);
		}

	}
}

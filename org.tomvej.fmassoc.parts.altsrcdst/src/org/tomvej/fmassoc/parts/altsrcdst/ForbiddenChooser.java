package org.tomvej.fmassoc.parts.altsrcdst;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

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
		checkColumn.getColumn().setResizable(false);

		TableLayoutSupport.create(table, 1, true, nameColumn, implNameColumn)
				.setupWidthColumn(checkColumn, 30, false, false);
	}

	public void setTables(Collection<Table> tables, Collection<Table> forbidden) {
		defaultForbidden = forbidden != null ? forbidden : Collections.emptySet();
	}

	public void setTableListener(Consumer<Set<Table>> listener) {
		tableListener = listener;
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

package org.tomvej.fmassoc.swt.tables;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TableColumn;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;

/**
 * Support for sorting by individual table columns.
 * Optionally may display "busy" indicator while sorting.
 * 
 * @author Tomáš Vejpustek
 */
@SuppressWarnings("rawtypes")
public class ColumnSortSupport {
	private final TableViewer table;
	private final Display display; // display to show busy indicator
	private List<SortEntry> sort = Collections.emptyList();
	private final Map<TableColumn, Comparator> comparators = new HashMap<>();
	private final ViewerComparator comparator = new ViewerComparator() {
		public int compare(Viewer viewer, Object e1, Object e2) {
			for (SortEntry entry : sort) {
				int result = ColumnSortSupport.this.compare(e1, e2, entry);
				if (result != 0) {
					return result;
				}
			}
			return 0;
		}
	};

	@SuppressWarnings("unchecked")
	private int compare(Object o1, Object o2, SortEntry sort) {
		int result = 0;

		Comparator custom = comparators.get(sort.getColumn());
		if (custom != null) {
			result = custom.compare(o1, o2);
		} else {
			CellLabelProvider labelProvider = table.getLabelProvider(table.getTable().indexOf(sort.getColumn()));
			if (labelProvider instanceof ColumnLabelProvider) {
				ColumnLabelProvider provider = (ColumnLabelProvider) labelProvider;
				result = provider.getText(o1).compareTo(provider.getText(o2));
			}
		}
		return sort.isAscending() ? result : -result;
	}


	/**
	 * Add column sort support to table.
	 */
	public ColumnSortSupport(TableViewer viewer) {
		this(viewer, null);
	}

	/**
	 * Add column sort support with busy indicator to table.
	 */
	public ColumnSortSupport(TableViewer viewer, Display display) {
		table = Validate.notNull(viewer);
		this.display = display; // can be null
		table.setComparator(comparator);
		Arrays.asList(table.getTable().getColumns()).forEach(this::addColumn);
		setDescending(false);
	}

	/**
	 * Set comparator for a table column.
	 * Note: comparator must compare table elements, not column values.
	 */
	public void setComparator(TableColumn column, Comparator comparator) {
		Validate.notNull(column);
		if (comparator != null) {
			comparators.put(column, comparator);
		} else {
			comparators.remove(column);
		}
		if (sort.stream().anyMatch(e -> e.getColumn().equals(column))) {
			table.refresh();
		}
	}

	/**
	 * Set comparator for a table column.
	 * Note: comparator must compare table elements, not column values.
	 */
	public void setComparator(TableViewerColumn column, Comparator comparator) {
		setComparator(column.getColumn(), comparator);
	}

	/**
	 * Add a supported column. This needs to be called only if the column has
	 * been added after the table was created.
	 */
	public void addColumn(TableColumn column) {
		column.addSelectionListener(new SelectionWrapper(e -> sortListener(column)));
	}

	private void sortListener(TableColumn column) {
		boolean descending = false;
		if (sort.size() == 1) {
			SortEntry entry = sort.get(0);
			if (entry.getColumn().equals(column)) {
				descending = entry.isAscending();
			}
		}
		sortByColumn(column, descending);
	}


	/**
	 * Sort the table by the first column.
	 */
	public void sortByFirstColumn() {
		sortByColumn(table.getTable().getColumn(0), false);
	}

	/**
	 * Sort table by target column.
	 */
	public void sortByColumn(TableColumn column, boolean descending) {
		Validate.notNull(column);
		sortByColumn(new SortEntry() {

			@Override
			public boolean isAscending() {
				return !descending;
			}

			@Override
			public TableColumn getColumn() {
				return column;
			}
		});
	}

	/**
	 * Sort table by target column.
	 */
	public void sortByColumn(TableViewerColumn column, boolean descending) {
		sortByColumn(column.getColumn(), descending);
	}

	/**
	 * Sort by target column.
	 */
	public void sortByColumn(SortEntry entry) {
		Validate.notNull(entry.getColumn());
		sort = Collections.singletonList(entry);
		setDescending(!entry.isAscending());
		table.getTable().setSortColumn(entry.getColumn());
		if (display == null) {
			table.refresh();
		} else {
			BusyIndicator.showWhile(display, table::refresh);
		}
	}

	private void setDescending(boolean descending) {
		table.getTable().setSortDirection(descending ? SWT.DOWN : SWT.UP);
	}

	/**
	 * Sort wrt a list of columns.
	 */
	public void multisort(List<SortEntry> columns) {
		Validate.noNullElements(columns);
		if (columns.size() == 1) {
			sortByColumn(columns.get(0));
		} else {
			sort = columns;
			table.getTable().setSortColumn(null);
			table.refresh();
		}
	}

}

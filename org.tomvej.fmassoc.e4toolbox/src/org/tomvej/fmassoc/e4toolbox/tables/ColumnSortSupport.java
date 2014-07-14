package org.tomvej.fmassoc.e4toolbox.tables;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableColumn;
import org.tomvej.fmassoc.e4toolbox.wrappers.SelectionWrapper;

/**
 * Support for sorting by individual table columns.
 * 
 * @author Tomáš Vejpustek
 */
public class ColumnSortSupport {
	private final TableViewer table;
	private TableColumn sortedBy;
	private boolean descending;
	private final Map<TableColumn, Comparator> comparators = new HashMap<>();
	private final ViewerComparator comparator = new ViewerComparator() {
		public int compare(Viewer viewer, Object e1, Object e2) {
			int result = 0;
			
			Comparator custom = comparators.get(sortedBy);
			if (custom != null) {
				result = custom.compare(e1, e2);
			} else if (sortedBy != null) {
				CellLabelProvider labelProvider = table.getLabelProvider(table.getTable().indexOf(sortedBy));
				if (labelProvider instanceof ColumnLabelProvider) {
					ColumnLabelProvider provider = (ColumnLabelProvider) labelProvider;
					result = provider.getText(e1).compareTo(provider.getText(e2));
				}
			}
			return descending ? -result : result;
		}
	};
	
	/**
	 * Add column sort support to table.
	 * 
	 * @param viewer Target table, no columns should be aded when this is invoked.
	 */
	public ColumnSortSupport(TableViewer viewer) {
		table = Validate.notNull(viewer);
		table.setComparator(comparator);
		build();
		setDescending(false);
	}
	
	/**
	 * Set comparator for a table column.
	 * Note: comparator must compare table elements, not column values. 
	 */
	public void setComparator(TableColumn column, Comparator comparator) {
		comparators.put(column, comparator);
		if (column != null && column.equals(sortedBy)) {
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
	 * Sort the table by the first column.
	 */
	public void sortByFirstColumn() {
		setDescending(false);
		sortedBy = table.getTable().getColumn(0);
		table.getTable().setSortColumn(sortedBy);
		table.refresh();
	}
	
	private void build() {
		for (TableColumn column : table.getTable().getColumns()) {
			column.addSelectionListener(new SelectionWrapper(event -> {
				if (column.equals(sortedBy)) {
					setDescending(!descending);
				} else {
					setDescending(false);
				}
				sortedBy = column;
				table.getTable().setSortColumn(sortedBy);
				table.refresh();
			}));
		}
	}
	
	private void setDescending(boolean descending) {
		this.descending = descending;
		table.getTable().setSortDirection(descending ? SWT.DOWN : SWT.UP);
	}
	
}

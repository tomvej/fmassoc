package org.tomvej.fmassoc.core.widgets.multisort;

import org.apache.commons.lang3.Validate;
import org.eclipse.swt.widgets.TableColumn;
import org.tomvej.fmassoc.core.tables.SortEntry;

class SimpleSortEntry implements SortEntry {
	private final TableColumn column;
	private boolean ascending = true;

	public SimpleSortEntry(TableColumn column) {
		this.column = Validate.notNull(column);
	}

	@Override
	public TableColumn getColumn() {
		return column;
	}

	@Override
	public boolean isAscending() {
		return ascending;
	}

	public void setAscending(boolean ascending) {
		this.ascending = ascending;
	}

	@Override
	public String toString() {
		return "[" + getColumn().getText() + ", " + (isAscending() ? "ascending" : "descending") + "]";
	}
}

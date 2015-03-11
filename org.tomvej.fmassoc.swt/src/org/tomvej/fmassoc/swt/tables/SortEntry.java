package org.tomvej.fmassoc.swt.tables;

import org.eclipse.swt.widgets.TableColumn;

/**
 * Information about column sorting.
 * 
 * @author Tomáš Vejpustek
 */
public interface SortEntry {
	/**
	 * Return table column.
	 */
	TableColumn getColumn();

	/**
	 * Return whether the sort order should be ascending ({@code true}) or
	 * descending ({@code false}).
	 */
	boolean isAscending();
}

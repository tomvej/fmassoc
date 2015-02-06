package org.tomvej.fmassoc.filter;

import java.util.function.Predicate;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Stores filter setting and creates visual components to change it.
 * 
 * @author Tomáš Vejpustek
 * @param <T>
 *            type of filtered values.
 */
public interface Filter<T> {

	/**
	 * Return current filter.
	 */
	Predicate<T> getFilter();

	/**
	 * Create or re-create visual component used to change current filter.
	 * There is an assumption only one of these components is active at any
	 * given time.
	 * 
	 * @param parent
	 *            parent composite
	 */
	Control createFilterPanel(Composite parent);
}

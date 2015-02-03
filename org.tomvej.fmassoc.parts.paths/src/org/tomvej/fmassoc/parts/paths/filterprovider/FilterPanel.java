package org.tomvej.fmassoc.parts.paths.filterprovider;

import java.util.function.Predicate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Panel for setting a filter. To be subclassed.
 * 
 * @author Tomáš Vejpustek
 * @param <T>
 *            Type of filtered values.
 */
public abstract class FilterPanel<T> extends Composite {

	/**
	 * Create parent with given parent. Does not allow to specify style (uses
	 * {@code SWT.NONE}).
	 * 
	 * @see Composite#Composite(Composite, int)
	 */
	public FilterPanel(Composite parent) {
		super(parent, SWT.NONE);
	}

	/**
	 * Get filter specified by this panel.
	 */
	public abstract Predicate<T> getFilter();

}

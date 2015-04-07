package org.tomvej.fmassoc.plugins.filters.basic.operator;

import java.util.function.Consumer;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Provides control used to specify values.
 * 
 * @author Tomáš Vejpustek
 * @see OperatorFilter
 * @param <T>
 *            type of specified values
 */
@FunctionalInterface
public interface ValueControlProvider<T> {

	/**
	 * Create control.
	 * 
	 * @param parent
	 *            parent composite
	 * @param listener
	 *            must be notified of value changes
	 * @param initial
	 *            initial value, can be {@code null}
	 */
	Control createControl(Composite parent, Consumer<T> listener, T initial);
}

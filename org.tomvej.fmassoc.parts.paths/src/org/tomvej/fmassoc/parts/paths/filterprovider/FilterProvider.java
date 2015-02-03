package org.tomvej.fmassoc.parts.paths.filterprovider;

import java.util.function.Function;

import org.eclipse.swt.widgets.Composite;


/**
 * Creates filter panels.
 * 
 * @author Tomáš Vejpustek
 * @param <T>
 */
public interface FilterProvider<T> extends Function<Composite, FilterPanel<T>> {

	/**
	 * Create new filter panel with supplied parent {@link Composite}.
	 */
	@Override
	public FilterPanel<T> apply(Composite t);
}

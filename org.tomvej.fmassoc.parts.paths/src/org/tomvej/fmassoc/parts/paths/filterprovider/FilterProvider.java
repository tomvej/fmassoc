package org.tomvej.fmassoc.parts.paths.filterprovider;

import java.util.function.Supplier;


/**
 * Creates filters.
 * 
 * @author Tomáš Vejpustek
 * @param <T>
 *            type of filtered values.
 */
public interface FilterProvider<T> extends Supplier<Filter<T>> {

}

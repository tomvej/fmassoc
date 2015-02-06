package org.tomvej.fmassoc.filter;

import java.util.function.Function;

/**
 * Context object which contains all loaded filters.
 * 
 * @author Tomáš Vejpustek
 */
public interface FilterRegistry extends Function<Class<?>, FilterProvider<?>> {

}

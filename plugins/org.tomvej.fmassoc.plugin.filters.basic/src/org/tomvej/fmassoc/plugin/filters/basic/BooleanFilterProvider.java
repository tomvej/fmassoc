package org.tomvej.fmassoc.plugin.filters.basic;

import org.tomvej.fmassoc.parts.paths.filterprovider.Filter;
import org.tomvej.fmassoc.parts.paths.filterprovider.FilterProvider;

/**
 * Provides {@link BooleanFilter}.
 * 
 * @author Tomáš Vejpustek
 */
public class BooleanFilterProvider implements FilterProvider<Boolean> {
	@Override
	public Filter<Boolean> get() {
		return new BooleanFilter();
	}
}
package org.tomvej.fmassoc.plugin.filters.basic;

import org.tomvej.fmassoc.model.db.Multiplicity;
import org.tomvej.fmassoc.parts.paths.filterprovider.Filter;
import org.tomvej.fmassoc.parts.paths.filterprovider.FilterProvider;

/**
 * Provides {@link MultiplicityFilter}.
 * 
 * @author Tomáš Vejpustek
 */
public class MultiplicityFilterProvider implements FilterProvider<Multiplicity> {

	@Override
	public Filter<Multiplicity> get() {
		return new MultiplicityFilter();
	}
}

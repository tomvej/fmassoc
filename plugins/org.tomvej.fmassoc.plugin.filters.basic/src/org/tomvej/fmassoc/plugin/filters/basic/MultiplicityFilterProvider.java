package org.tomvej.fmassoc.plugin.filters.basic;

import org.tomvej.fmassoc.filter.Filter;
import org.tomvej.fmassoc.filter.FilterProvider;
import org.tomvej.fmassoc.model.db.Multiplicity;

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

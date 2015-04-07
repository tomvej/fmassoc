package org.tomvej.fmassoc.plugin.prioritydfpathfinder;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.core.search.PathFinder;
import org.tomvej.fmassoc.core.search.PathFinderProvider;
import org.tomvej.fmassoc.core.search.SearchInput;

/**
 * Provides pruning-enabled depth-first path finders.
 * 
 * @author Tomáš Vejpustek
 * @see PriorityDFPathFinder
 */
public class PriorityDFFinderProvider implements PathFinderProvider {
	private final Pruning prune;

	/**
	 * Specify pruning.
	 */
	public PriorityDFFinderProvider(Pruning pruning) {
		prune = Validate.notNull(pruning);
	}

	@Override
	public PathFinder createPathFinder(SearchInput input) {
		return new PriorityDFPathFinder(prune, input.getSource(), input.getDestinations(), input.getForbidden());
	}
}

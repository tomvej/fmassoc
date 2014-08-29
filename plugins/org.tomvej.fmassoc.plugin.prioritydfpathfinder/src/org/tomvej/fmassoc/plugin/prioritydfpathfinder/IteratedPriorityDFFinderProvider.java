package org.tomvej.fmassoc.plugin.prioritydfpathfinder;

import java.util.Arrays;
import java.util.Collections;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.core.search.PathFinder;
import org.tomvej.fmassoc.core.search.PathFinderProvider;
import org.tomvej.fmassoc.core.search.SearchInput;

/**
 * Provides depth-first path finders which iterate pruning.
 * 
 * @author Tomáš Vejpustek
 * @see IteratedPriorityDFPathFinder
 */
public class IteratedPriorityDFFinderProvider implements PathFinderProvider {
	private final Iterable<Pruning> pruning;

	/**
	 * Specify pruning sequence.
	 */
	public IteratedPriorityDFFinderProvider(Iterable<Pruning> pruning) {
		this.pruning = Validate.noNullElements(pruning);
	}

	/**
	 * Specify pruning sequence.
	 */
	public IteratedPriorityDFFinderProvider(Pruning... pruning) {
		this(Arrays.asList(pruning));
	}

	@Override
	public PathFinder createPathFinder(SearchInput input) {
		return new IteratedPriorityDFPathFinder(pruning, input.getSource(), input.getDestinations(), Collections.emptySet());
	}

}

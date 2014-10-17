package org.tomvej.fmassoc.plugin.dfpathfinder;

import org.tomvej.fmassoc.core.search.PathFinder;
import org.tomvej.fmassoc.core.search.PathFinderProvider;
import org.tomvej.fmassoc.core.search.SearchInput;

/**
 * Provides instances of {@link DFPathFinder}.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class DFPathFinderProvider implements PathFinderProvider {

	@Override
	public PathFinder createPathFinder(SearchInput input) {
		return new DFPathFinder(input.getSource(), input.getDestinations(), input.getForbidden());
	};

	@Override
	public String toString() {
		return "Unpruned Depth-First Path Finder";
	}
}

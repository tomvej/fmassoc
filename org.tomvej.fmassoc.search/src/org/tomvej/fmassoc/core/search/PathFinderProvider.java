package org.tomvej.fmassoc.core.search;

/**
 * Provider of path searching algorithm.
 * 
 * @author Tomáš Vejpustek
 */
@FunctionalInterface
public interface PathFinderProvider {

	/**
	 * Create job which finds paths between specified source and destinations
	 * tables.
	 */
	PathFinder createPathFinder(SearchInput input);
}

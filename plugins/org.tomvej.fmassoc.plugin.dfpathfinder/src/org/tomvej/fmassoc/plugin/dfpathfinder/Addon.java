package org.tomvej.fmassoc.plugin.dfpathfinder;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.tomvej.fmassoc.core.search.PathFinderProvider;

/**
 * Puts depth-first path finder provider into context so that it can be used for
 * search.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class Addon {

	/**
	 * Register path finder provider.
	 */
	@PostConstruct
	public void setupPathFinderProvider(IEclipseContext context) {
		context.set(PathFinderProvider.class, new DFPathFinderProvider());
	}

}
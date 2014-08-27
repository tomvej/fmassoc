package org.tomvej.fmassoc.plugin.dfpathfinder;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.tomvej.fmassoc.core.search.PathFinderProvider;

public class Addon {

	@PostConstruct
	public void setupPathFinderProvider(IEclipseContext context) {
		context.set(PathFinderProvider.class, new DFPathFinderProvider());
	}

}
package org.tomvej.fmassoc.core.search.internal;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.tomvej.fmassoc.core.search.PathFinderProvider;
import org.tomvej.fmassoc.core.search.SearchInput;

public class SearchPaths {

	@Execute
	public void execute(SearchInput input, PathFinderProvider provider, IEclipseContext context, IEventBroker broker) {
		PathFinderJob job = new PathFinderJob(provider.createPathFinder(input), broker);
		context.set(PathFinderJob.class, job);
		job.schedule();
	}

	@Optional
	@CanExecute
	public boolean canExecute(SearchInput input, PathFinderProvider provider) {
		return input != null && provider != null;
	}
}
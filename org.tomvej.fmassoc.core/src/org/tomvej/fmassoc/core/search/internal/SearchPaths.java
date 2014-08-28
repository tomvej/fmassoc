package org.tomvej.fmassoc.core.search.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.search.PathFinderProvider;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.model.path.Path;

public class SearchPaths {
	private final List<Path> foundPaths;

	@Inject
	public SearchPaths(IEclipseContext context) {
		foundPaths = new ArrayList<>();
		context.set(ContextObjects.FOUND_PATHS, Collections.unmodifiableList(foundPaths));
	}

	@Execute
	public void execute(SearchInput input, PathFinderProvider provider, IEclipseContext context, IEventBroker broker) {
		PathFinderJob job = new PathFinderJob(provider.createPathFinder(input), broker, foundPaths);
		context.set(PathFinderJob.class, job);
		job.schedule();
	}

	@Optional
	@CanExecute
	public boolean canExecute(SearchInput input, PathFinderProvider provider) {
		return input != null && provider != null;
	}
}
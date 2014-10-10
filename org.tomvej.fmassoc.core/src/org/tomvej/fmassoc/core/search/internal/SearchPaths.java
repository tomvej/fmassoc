package org.tomvej.fmassoc.core.search.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.communicate.PathSearchTopic;
import org.tomvej.fmassoc.core.search.PathFinderProvider;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.model.path.Path;

public class SearchPaths {
	private final List<Path> foundPaths;

	// the context must be shared -- otherwise, the job might not be removed
	// from the right context
	@Inject
	private IEclipseContext context;


	@Inject
	public SearchPaths(IEclipseContext context) {
		foundPaths = new ArrayList<>();
		context.set(ContextObjects.FOUND_PATHS, Collections.unmodifiableList(foundPaths));
	}

	@Execute
	public void execute(SearchInput input, PathFinderProvider provider, IEventBroker broker, Logger logger) {
		PathFinderJob job = new PathFinderJob(provider.createPathFinder(input), broker, foundPaths, logger);
		context.set(PathFinderJob.class, job);
		foundPaths.clear();
		broker.post(PathSearchTopic.START, input);
		// TODO probably will need to put current search
		// input into context (named)
		job.schedule();
	}

	@Optional
	@CanExecute
	public boolean canExecute(SearchInput input, PathFinderProvider provider, PathFinderJob job) {
		return input != null && provider != null && job == null;
	}

	@Inject
	public void jobFinished(@Optional @EventTopic(PathSearchTopic.FINISH) IStatus status) {
		context.set(PathFinderJob.class, null);
	}

	@Inject
	public void jobCancelled(@Optional @EventTopic(PathSearchTopic.CANCEL) IStatus status) {
		context.set(PathFinderJob.class, null);
	}

}
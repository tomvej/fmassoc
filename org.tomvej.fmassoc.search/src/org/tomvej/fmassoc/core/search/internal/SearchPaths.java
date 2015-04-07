package org.tomvej.fmassoc.core.search.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.search.PathFinderProvider;
import org.tomvej.fmassoc.core.search.PathSearchTopic;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Handles for path search.
 * 
 * @author Tomáš Vejpustek
 */
public class SearchPaths {
	private final List<Path> foundPaths;

	// the context must be shared -- otherwise, the job might not be removed
	// from the right context
	private IEclipseContext context;


	/**
	 * Put found path list into context.
	 */
	@Inject
	public SearchPaths(IEclipseContext context) {
		this.context = context;
		foundPaths = new ArrayList<>();
		context.set(ContextObjects.FOUND_PATHS, Collections.unmodifiableList(foundPaths));
	}

	/**
	 * Start a path search.
	 */
	@Execute
	public void execute(@Optional SearchInput input, @Optional PathFinderProvider provider, IEventBroker broker,
			Logger logger, @Optional PathFinderJob currentJob) {
		/* check objects */
		List<String> warnings = new ArrayList<>();
		if (currentJob != null) {
			warnings.add("search job already running");
		}
		if (input == null) {
			warnings.add("no search input");
		}
		if (provider == null) {
			warnings.add("no path finder");
		}

		if (!warnings.isEmpty()) {
			logger.warn(warnings.stream().collect(Collectors.joining(", ", "Cannot start path search: ", "")));
			return;
		}

		/* actual handler */
		PathFinderJob job = new PathFinderJob(provider.createPathFinder(input), broker, foundPaths, logger);
		context.set(PathFinderJob.class, job);
		foundPaths.clear();
		broker.post(PathSearchTopic.START, input);
		// TODO probably will need to put current search
		// input into context (named)
		job.schedule();
	}

	/**
	 * Clean-up context when job is finished.
	 */
	@Inject
	@Optional
	public void jobFinished(@EventTopic(PathSearchTopic.FINISH) IStatus status) {
		context.remove(PathFinderJob.class);
	}

	/**
	 * Clean-up context when job is cancelled.
	 */
	@Inject
	@Optional
	public void jobCancelled(@EventTopic(PathSearchTopic.CANCEL) IStatus status) {
		context.remove(PathFinderJob.class);
	}

}
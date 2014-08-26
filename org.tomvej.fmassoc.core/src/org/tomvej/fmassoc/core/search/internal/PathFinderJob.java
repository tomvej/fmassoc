package org.tomvej.fmassoc.core.search.internal;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.tomvej.fmassoc.core.communicate.PathSearchTopic;
import org.tomvej.fmassoc.core.search.PathFinder;

public class PathFinderJob extends Job {
	private final PathFinder finder;
	private final IEventBroker eventBroker;


	public PathFinderJob(PathFinder finder, IEventBroker eventBroker) {
		super("Path search " + finder.toString());
		this.finder = Validate.notNull(finder);
		this.eventBroker = Validate.notNull(eventBroker);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// TODO this is perhaps too simple -- we'll see
		return finder.run(path -> eventBroker.post(PathSearchTopic.PUBLISH, path), monitor);
	}

}

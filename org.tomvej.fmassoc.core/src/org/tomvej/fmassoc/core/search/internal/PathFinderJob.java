package org.tomvej.fmassoc.core.search.internal;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.tomvej.fmassoc.core.communicate.PathSearchTopic;
import org.tomvej.fmassoc.core.search.PathFinder;
import org.tomvej.fmassoc.model.path.Path;

public class PathFinderJob extends Job {
	private final PathFinder finder;
	private final IEventBroker eventBroker;
	private final List<Path> found;


	public PathFinderJob(PathFinder finder, IEventBroker eventBroker, List<Path> foundPaths) {
		super("Path search " + finder.toString());
		this.finder = Validate.notNull(finder);
		this.eventBroker = Validate.notNull(eventBroker);
		found = Validate.notNull(foundPaths);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		try {
			IStatus result = finder.run(this::publish, monitor);
			if (result.isOK()) { // what about erroneous states?
				eventBroker.post(PathSearchTopic.FINISH, result);
			} else {
				eventBroker.post(PathSearchTopic.CANCEL, result);
			}
			return result;
		} catch (OperationCanceledException oce) {
			eventBroker.post(PathSearchTopic.CANCEL, Status.CANCEL_STATUS);
			return Status.CANCEL_STATUS;
		}
	}

	private void publish(Path target) {
		found.add(target);
		eventBroker.post(PathSearchTopic.PUBLISH, target);
	}

}

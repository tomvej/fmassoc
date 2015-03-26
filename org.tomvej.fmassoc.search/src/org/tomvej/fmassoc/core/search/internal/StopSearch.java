package org.tomvej.fmassoc.core.search.internal;

import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.tomvej.fmassoc.core.search.PathSearchTopic;

/**
 * Handler for path search interruption.
 * 
 * @author Tomáš Vejpustek
 */
public class StopSearch {

	/**
	 * Interrupt the currently running job.
	 */
	@Execute
	public void execute(@Optional PathFinderJob currentJob, IEventBroker broker, Logger logger) {
		if (currentJob == null) {
			logger.warn("Cannot stop path search job: none running");
		} else if (currentJob.cancel()) {
			// the job has not started yet, so it will not send cancel
			broker.post(PathSearchTopic.CANCEL, Status.CANCEL_STATUS);
		}
	}

}
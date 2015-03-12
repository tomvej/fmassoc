package org.tomvej.fmassoc.core.search.internal;

import org.eclipse.core.runtime.Status;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.events.IEventBroker;
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
	public void execute(PathFinderJob currentJob, IEventBroker broker) {
		if (currentJob.cancel()) {
			// the job has not started yet, so it will not send cancel
			broker.post(PathSearchTopic.CANCEL, Status.CANCEL_STATUS);
		}
	}

	/**
	 * Check if there is a job running.
	 */
	@CanExecute
	public boolean canExecute(PathFinderJob currentJob) {
		return currentJob != null;
	}

}
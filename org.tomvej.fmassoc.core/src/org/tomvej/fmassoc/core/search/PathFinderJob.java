package org.tomvej.fmassoc.core.search;

import java.util.function.Consumer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.jobs.Job;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Job which searches for paths and publishes the found ones.
 * 
 * @author Tomáš Vejpustek
 * @see PathFinderProvider
 *
 */
@FunctionalInterface
public interface PathFinderJob {

	/**
	 * Search for paths. Implementations should check {@code monitor} for
	 * cancellation (as described by {@link Job} API). The {@code monitor} can
	 * be used to update of jobs status.
	 * 
	 * @param publisher
	 *            Used to publish found paths.
	 * @return Status of the job on finish.
	 */
	IStatus run(Consumer<Path> publisher, IProgressMonitor monitor);
}
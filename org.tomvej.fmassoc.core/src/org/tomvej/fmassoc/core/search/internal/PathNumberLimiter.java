package org.tomvej.fmassoc.core.search.internal;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.di.extensions.Preference;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.communicate.PathSearchTopic;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.model.path.Path;

public class PathNumberLimiter {
	private boolean running;

	@Inject
	@Optional
	public void searchStarted(@EventTopic(PathSearchTopic.START) SearchInput input) {
		if (input != null) {
			running = true;
		}
	}

	@Inject
	@Optional
	public void searchFinished(@EventTopic(PathSearchTopic.FINISH) IStatus status) {
		running = false;
	}

	@Inject
	@Optional
	public void searchCancelled(@EventTopic(PathSearchTopic.CANCEL) IStatus status) {
		running = false;
	}

	@Inject
	public void pathFound(
			@Optional @EventTopic(PathSearchTopic.PUBLISH) Path target,
			@Named(ContextObjects.FOUND_PATHS) List<Path> paths,
			@Optional PathFinderJob job,
			@Preference(nodePath = PathSearchPreference.NODE, value = PathSearchPreference.PATH_LIMIT) Integer limit,
			@Preference(nodePath = PathSearchPreference.NODE, value = PathSearchPreference.SHOW_PATH_LIMIT_REACHED) Boolean showDialog) {
		if (running && paths.size() >= limit) {
			job.cancel();
			running = false;
			if (showDialog) {
				// FIXME execute the command
			}
		}
	}

}

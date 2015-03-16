package org.tomvej.fmassoc.core.search.limit;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.di.extensions.Preference;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.search.PathSearchTopic;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.core.search.internal.PathFinderJob;
import org.tomvej.fmassoc.core.search.preference.PathSearchPreference;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Limits number of found paths, to prevent heap overflow, etc.
 * 
 * @author Tomáš Vejpustek
 */
public class PathNumberLimiter {
	private boolean running;

	/**
	 * Listen for search being started.
	 */
	@Inject
	@Optional
	public void searchStarted(@EventTopic(PathSearchTopic.START) SearchInput input) {
		if (input != null) {
			running = true;
		}
	}

	/**
	 * Listen for search finishing.
	 */
	@Inject
	@Optional
	public void searchFinished(@EventTopic(PathSearchTopic.FINISH) IStatus status) {
		running = false;
	}

	/**
	 * Listen for search being cancelled.
	 */
	@Inject
	@Optional
	public void searchCancelled(@EventTopic(PathSearchTopic.CANCEL) IStatus status) {
		running = false;
	}

	/**
	 * Listen for path found. Terminate if there are too many paths.
	 */
	@Inject
	@Optional
	public void pathFound(
			@EventTopic(PathSearchTopic.PUBLISH) Path target,
			@Named(ContextObjects.FOUND_PATHS) List<Path> paths,
			PathFinderJob job,
			EHandlerService handlers,
			ECommandService commands,
			@Preference(nodePath = PathSearchPreference.NODE, value = PathSearchPreference.PATH_LIMIT) Integer limit,
			@Preference(nodePath = PathSearchPreference.NODE, value = PathSearchPreference.SHOW_PATH_LIMIT_REACHED) Boolean showDialog) {
		if (running && paths.size() >= limit) {
			job.cancel();
			running = false;
			if (showDialog) {
				handlers.executeHandler(commands.createCommand("org.tomvej.fmassoc.search.command.pathlimitreached",
						Collections.emptyMap()));
			}
		}
	}

}

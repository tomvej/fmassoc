package org.tomvej.fmassoc.test.path.tables;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.log.Logger;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.communicate.PathSearchTopic;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.model.path.Path;

public class PathChecker {
	@Inject
	private Logger logger;

	@Inject
	@Optional
	public void searchFinished(@EventTopic(PathSearchTopic.FINISH) Object result, SearchInput input,
			@Named(ContextObjects.FOUND_PATHS) List<Path> paths) {
		checkPaths(paths, input);
	}

	@Inject
	@Optional
	public void searchCancelled(@EventTopic(PathSearchTopic.CANCEL) Object result, SearchInput input,
			@Named(ContextObjects.FOUND_PATHS) List<Path> paths) {
		checkPaths(paths, input);
	}

	private void checkPaths(List<Path> paths, SearchInput input) {
		for (Path path : paths) {
			List<String> errors = new SinglePathChecker(path, input).getErrors();
			if (!errors.isEmpty()) {
				StringBuilder msg = new StringBuilder("Errors on path [").append(path.getSource().getName());
				path.getAssociations().forEach(a -> msg.append(" " + a.getName() + " " + a.getDestination().getName()));
				msg.append("]:");
				errors.forEach(e -> msg.append("\n").append(e));
				logger.error(msg.toString());
			}
		}
	}
}
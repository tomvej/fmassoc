package org.tomvej.fmassoc.core.search.internal;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;

public class StopSearch {

	@Execute
	public void execute(PathFinderJob currentJob, IEclipseContext context) {
		if (currentJob.cancel()) {
			context.remove(PathFinderJob.class);
		}
	}

	@CanExecute
	public boolean canExecute(PathFinderJob currentJob) {
		// some other check? is not already cancelled?
		return currentJob != null;
	}

}
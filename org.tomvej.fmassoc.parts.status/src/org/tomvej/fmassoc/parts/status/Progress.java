package org.tomvej.fmassoc.parts.status;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.tomvej.fmassoc.core.search.PathSearchTopic;
import org.tomvej.fmassoc.core.search.SearchInput;

/**
 * Progress bar for status bar.
 * 
 * @author Tomáš Vejpustek
 */
public class Progress {
	private ProgressBar bar;

	/**
	 * Initialize UI components.
	 */
	@PostConstruct
	public void createComponents(Composite parent) {
		bar = new ProgressBar(parent, SWT.HORIZONTAL | SWT.SMOOTH | SWT.INDETERMINATE);
		bar.setState(SWT.PAUSED);
	}

	/**
	 * Listen for search start.
	 */
	@Inject
	@Optional
	public void searchStarted(@UIEventTopic(PathSearchTopic.START) SearchInput input) {
		bar.setState(SWT.NORMAL);
	}

	/**
	 * Listen for search cancel.
	 */
	@Inject
	@Optional
	public void searchCancelled(@UIEventTopic(PathSearchTopic.CANCEL) IStatus status) {
		bar.setState(SWT.PAUSED);
	}

	/**
	 * Listen for search completion.
	 */
	@Inject
	@Optional
	public void searchFinished(@UIEventTopic(PathSearchTopic.FINISH) IStatus status) {
		bar.setState(SWT.PAUSED);
	}

}

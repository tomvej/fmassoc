package org.tomvej.fmassoc.parts.status;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.tomvej.fmassoc.core.search.PathSearchTopic;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.swt.widgets.CompoundProgressBar;

/**
 * Progress bar for status bar.
 * 
 * @author Tomáš Vejpustek
 */
public class Progress {
	private CompoundProgressBar bar;

	/**
	 * Initialize UI components.
	 */
	@PostConstruct
	public void createComponents(Composite parent) {
		bar = new CompoundProgressBar(parent, SWT.HORIZONTAL | SWT.SMOOTH);
		bar.setDeterminate(true);
		bar.setValues(0, 100, 0);
	}

	/**
	 * Listen for search start.
	 */
	@Inject
	@Optional
	public void searchStarted(@UIEventTopic(PathSearchTopic.START) SearchInput input) {
		bar.setDeterminate(false);
		bar.setSelection(0);
	}

	/**
	 * Listen for search cancel.
	 */
	@Inject
	@Optional
	public void searchCancelled(@UIEventTopic(PathSearchTopic.CANCEL) IStatus status) {
		bar.setDeterminate(true);
		bar.setSelection(50);
	}

	/**
	 * Listen for search completion.
	 */
	@Inject
	@Optional
	public void searchFinished(@UIEventTopic(PathSearchTopic.FINISH) IStatus status) {
		bar.setDeterminate(true);
		bar.setSelection(100);
	}

}

package org.tomvej.fmassoc.parts.status;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.search.PathFinderProvider;
import org.tomvej.fmassoc.core.search.PathSearchTopic;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Shows current search information.
 * 
 * @author Tomáš Vejpustek
 */
public class SearchStatus {
	private Label status;
	private Composite parent;
	private SearchInput input;

	/**
	 * Initialize UI.
	 */
	@PostConstruct
	public void createComponents(Composite parent) {
		this.parent = parent;
		status = new Label(parent, SWT.NONE);
	}

	private void setText(String text) {
		status.setText(text);
		parent.pack();
	}

	private String formatInput() {
		return "from " + input.getSource().getImplName() + " to "
				+ input.getDestinations().get(input.getDestinations().size() - 1).getImplName();
	}

	/**
	 * Listen for search start.
	 */
	@Inject
	@Optional
	public void searchStarted(@UIEventTopic(PathSearchTopic.START) SearchInput input, PathFinderProvider provider) {
		this.input = input;
		setText("Searching for paths " + formatInput() + ".");
		status.setToolTipText(input + "\n" + provider);
	}

	/**
	 * Listen for search completion.
	 */
	@Inject
	@Optional
	public void searchFinished(@UIEventTopic(PathSearchTopic.FINISH) IStatus status,
			@Named(ContextObjects.FOUND_PATHS) List<Path> paths) {
		setText("Showing paths " + formatInput() + "(" + paths.size() + " found).");
	}

	/**
	 * Listen for search cancel.
	 */
	@Inject
	@Optional
	public void searchCancelled(@UIEventTopic(PathSearchTopic.CANCEL) IStatus status,
			@Named(ContextObjects.FOUND_PATHS) List<Path> paths) {
		setText("Showing paths " + formatInput() + "(" + paths.size() + " found; interrupted).");
	}

}

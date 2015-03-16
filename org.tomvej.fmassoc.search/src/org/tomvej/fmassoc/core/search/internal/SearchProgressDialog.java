package org.tomvej.fmassoc.core.search.internal;

import java.util.Collections;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MDialog;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.osgi.service.prefs.BackingStoreException;
import org.tomvej.fmassoc.core.search.PathFinderProvider;
import org.tomvej.fmassoc.core.search.PathSearchTopic;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.core.search.preference.PathSearchPreference;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;

/**
 * Dialog (optionally) displayed during path search. Displays sort settings and
 * can be used to cancel the search.
 * 
 * @author Tomáš Vejpustek
 */
public class SearchProgressDialog {
	private Button alwaysBgBtn;
	private Label statusLbl;

	/**
	 * Create dialog area.
	 */
	@Inject
	public void createControls(Composite parent, EHandlerService handlers, ECommandService commands, MDialog dialog,
			@Preference(nodePath = PathSearchPreference.NODE) IEclipsePreferences searchPreference) {
		parent.setLayout(new GridLayout(2, false));
		statusLbl = new Label(parent, SWT.WRAP);
		statusLbl.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, true).create());

		ProgressBar progress = new ProgressBar(parent, SWT.HORIZONTAL | SWT.SMOOTH | SWT.INDETERMINATE);
		progress.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, false).create());

		alwaysBgBtn = new Button(parent, SWT.CHECK);
		alwaysBgBtn.setText("Show search progress dialog.");
		alwaysBgBtn.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

		Button bgBtn = new Button(parent, SWT.PUSH);
		bgBtn.setText("Run in background");
		bgBtn.setLayoutData(GridDataFactory.swtDefaults().align(SWT.RIGHT, SWT.FILL).grab(true, false).create());

		Button cancelBtn = new Button(parent, SWT.PUSH);
		cancelBtn.setText("Cancel");
		cancelBtn.setLayoutData(GridDataFactory.fillDefaults().create());

		alwaysBgBtn.addSelectionListener(new SelectionWrapper(
				e -> searchPreference.putBoolean(PathSearchPreference.SHOW_SEARCH_PROGRESS_DIALOG,
						alwaysBgBtn.getSelection())));
		bgBtn.addSelectionListener(new SelectionWrapper(e -> dialog.setVisible(false)));
		cancelBtn.addSelectionListener(new SelectionWrapper(
				e -> handlers.executeHandler(commands.createCommand(
						"org.tomvej.fmassoc.search.command.stopsearch", Collections.emptyMap()))));
	}

	/**
	 * Show dialog when search is started.
	 */
	@Optional
	@Inject
	public void searchStarted(
			@UIEventTopic(PathSearchTopic.START) SearchInput input,
			MDialog dialog,
			MPart part,
			PathFinderProvider provider,
			@Preference(nodePath = PathSearchPreference.NODE, value = PathSearchPreference.SHOW_SEARCH_PROGRESS_DIALOG) Boolean show) {
		if (show) {
			dialog.setVisible(true);
			part.setVisible(true);
			alwaysBgBtn.setSelection(true);
			statusLbl.setText("Searching paths through: " + formatInput(input) + "\n\nAlgorithm: " + provider);
		}
	}

	/**
	 * Hide dialog when search is finished.
	 */
	@Optional
	@Inject
	public void searchFinished(@UIEventTopic(PathSearchTopic.FINISH) IStatus status, MDialog dialog) {
		dialog.setVisible(false);
	}

	/**
	 * Hide dialog when search is cancelled.
	 */
	@Optional
	@Inject
	public void searchCancelled(@UIEventTopic(PathSearchTopic.CANCEL) IStatus status, MDialog dialog) {
		dialog.setVisible(false);
	}

	/**
	 * Store dialog preferences.
	 */
	@PersistState
	public void persistState(@Preference(nodePath = PathSearchPreference.NODE) IEclipsePreferences searchPreference,
			Logger logger) {
		try {
			searchPreference.flush();
		} catch (BackingStoreException e) {
			logger.error(e, "Cannot store path search preference.");
		}
	}

	private String formatInput(SearchInput input) {
		StringBuilder result = new StringBuilder(input.getSource().getImplName());
		for (Table dest : input.getDestinations()) {
			result.append(" -> ").append(dest.getImplName());
		}
		if (!input.getForbidden().isEmpty()) {
			result.append(input.getForbidden().stream().map(t -> t.getImplName())
					.collect(Collectors.joining(", ", "(skipping ", ")")));
		}
		return result.append(".").toString();
	}
}

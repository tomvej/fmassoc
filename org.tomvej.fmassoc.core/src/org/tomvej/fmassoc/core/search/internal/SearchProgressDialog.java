package org.tomvej.fmassoc.core.search.internal;

import java.util.Collections;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.osgi.service.prefs.BackingStoreException;
import org.tomvej.fmassoc.core.communicate.PathSearchTopic;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;

public class SearchProgressDialog {
	private Button alwaysBgBtn;

	@Inject
	public void createControls(Composite parent, EHandlerService handlers, ECommandService commands, MDialog dialog,
			@Preference(nodePath = PathSearchPreference.NODE) IEclipsePreferences searchPreference) {
		new ProgressBar(parent, SWT.HORIZONTAL | SWT.SMOOTH | SWT.INDETERMINATE);

		alwaysBgBtn = new Button(parent, SWT.CHECK);
		alwaysBgBtn.setText("Show search progress dialog.");

		Button bgBtn = new Button(parent, SWT.PUSH);
		bgBtn.setText("Run in background");

		Button cancelBtn = new Button(parent, SWT.PUSH);
		cancelBtn.setText("Cancel");

		alwaysBgBtn.addSelectionListener(new SelectionWrapper(
				e -> searchPreference.putBoolean(PathSearchPreference.SHOW_SEARCH_PROGRESS_DIALOG,
						alwaysBgBtn.getSelection())));
		bgBtn.addSelectionListener(new SelectionWrapper(e -> dialog.setVisible(false)));
		cancelBtn.addSelectionListener(new SelectionWrapper(
				e -> handlers.executeHandler(commands.createCommand(
						"org.tomvej.fmassoc.core.command.stopsearch", Collections.emptyMap()))));
	}

	@Optional
	@Inject
	public void searchStarted(
			@UIEventTopic(PathSearchTopic.START) SearchInput input,
			MDialog dialog,
			@Preference(nodePath = PathSearchPreference.NODE, value = PathSearchPreference.SHOW_SEARCH_PROGRESS_DIALOG) Boolean show) {
		if (show) {
			dialog.setVisible(true);
			alwaysBgBtn.setSelection(true);
		}
	}

	@Optional
	@Inject
	public void searchFinished(@UIEventTopic(PathSearchTopic.FINISH) IStatus status, MDialog dialog) {
		dialog.setVisible(false);
	}

	@Optional
	@Inject
	public void searchCancelled(@UIEventTopic(PathSearchTopic.CANCEL) IStatus status, MDialog dialog) {
		dialog.setVisible(false);
	}

	@PersistState
	public void persistState(@Preference(nodePath = PathSearchPreference.NODE) IEclipsePreferences searchPreference,
			Logger logger) {
		try {
			searchPreference.flush();
		} catch (BackingStoreException e) {
			logger.error(e, "Cannot store path search preference.");
		}
	}
}

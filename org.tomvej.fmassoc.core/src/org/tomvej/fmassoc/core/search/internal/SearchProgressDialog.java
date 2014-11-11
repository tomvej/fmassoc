package org.tomvej.fmassoc.core.search.internal;

import java.util.Collections;

import javax.inject.Inject;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ProgressBar;
import org.tomvej.fmassoc.core.communicate.PathSearchTopic;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;

public class SearchProgressDialog {


	@Inject
	public void createControls(Composite parent, EHandlerService handlers, ECommandService commands, MDialog dialog) {
		new ProgressBar(parent, SWT.HORIZONTAL | SWT.SMOOTH | SWT.INDETERMINATE);

		Button alwaysBgBtn = new Button(parent, SWT.CHECK);
		alwaysBgBtn.setText("Always run in background.");

		Button bgBtn = new Button(parent, SWT.PUSH);
		bgBtn.setText("Run in background");

		Button cancelBtn = new Button(parent, SWT.PUSH);
		cancelBtn.setText("Cancel");

		bgBtn.addSelectionListener(new SelectionWrapper(e -> dialog.setVisible(false)));
		cancelBtn.addSelectionListener(new SelectionWrapper(
				e -> handlers.executeHandler(commands.createCommand(
						"org.tomvej.fmassoc.core.command.stopsearch", Collections.emptyMap()))));
	}

	@Optional
	@Inject
	public void searchStarted(@UIEventTopic(PathSearchTopic.START) SearchInput input, MDialog dialog) {
		dialog.setVisible(true);
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
}

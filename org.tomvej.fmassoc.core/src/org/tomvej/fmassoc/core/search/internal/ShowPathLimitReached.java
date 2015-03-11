package org.tomvej.fmassoc.core.search.internal;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;

/**
 * Handler which shows "too many paths found, search interrupted" dialog.
 * 
 * @author Tomáš Vejpustek
 */
public class ShowPathLimitReached {
	private static final String MESSAGE = "There are too many paths for given search input. "
			+ "The search was interrupted after %d paths had been found.";

	@Inject
	@Preference(nodePath = PathSearchPreference.NODE)
	private IEclipsePreferences preference;

	private class Dialog extends MessageDialog {

		public Dialog(Shell shell, int limit) {
			super(shell, "Path Search Interrupted", null, String.format(MESSAGE, limit), MessageDialog.WARNING,
					new String[] { "OK" }, 0);
		}

		@Override
		protected Control createCustomArea(Composite parent) {
			Button check = new Button(parent, SWT.CHECK);
			check.setText("Do not show this dialog again.");
			check.addSelectionListener(new SelectionWrapper(e -> preference.putBoolean(
					PathSearchPreference.SHOW_PATH_LIMIT_REACHED, !check.getSelection())));
			return check;
		}
	}

	/**
	 * Display the dialog.
	 */
	@Execute
	public void execute(Shell currentShell, UISynchronize sync, Logger logger,
			@Named(ContextObjects.FOUND_PATHS) List<Path> paths) {
		sync.asyncExec(() -> {
			new Dialog(currentShell, paths.size()).open();
			try {
				preference.flush();
			} catch (Exception e) {
				logger.error(e, "Cannot store dialog preference.");
			}
		});
	}
}

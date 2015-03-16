package org.tomvej.fmassoc.core.search.preference;

import org.eclipse.core.runtime.preferences.DefaultScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.osgi.service.prefs.BackingStoreException;
import org.tomvej.fmassoc.core.preference.ContextPreferencePage;

/**
 * Search preference page. Allows to specify:
 * <ul>
 * <li>Path number limit,</li>
 * <li>whether to show message dialog after the path number limit has been
 * found.</li>
 * </ul>
 * 
 * @author Tomáš Vejpustek
 */
public class PathSearchPreferencePage extends PreferencePage implements ContextPreferencePage {
	private IEclipsePreferences defPreference = DefaultScope.INSTANCE.getNode(PathSearchPreference.NODE);
	private IEclipsePreferences preference = InstanceScope.INSTANCE.getNode(PathSearchPreference.NODE);

	private int getInt(String key) {
		if (preference.get(key, null) == null) {
			return defPreference.getInt(key, 0);
		} else {
			return preference.getInt(key, 0);
		}
	}

	private boolean getBool(String key) {
		if (preference.get(key, null) == null) {
			return defPreference.getBoolean(key, false);
		} else {
			return preference.getBoolean(key, false);
		}
	}

	private Button showPathLimitDialog;
	private Spinner pathLimit;
	private Button showSearchProgressDialog;

	/**
	 * Create this preference page.
	 */
	public PathSearchPreferencePage() {
		super("Search");
	}

	@Override
	public void init(IEclipseContext context) {}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new RowLayout(SWT.VERTICAL));

		Composite pathLimitComposite = new Composite(container, SWT.NONE);
		pathLimitComposite.setLayout(new GridLayout(2, false));
		GridDataFactory layout = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER);

		Label pathLimitLabel = new Label(pathLimitComposite, SWT.NONE);
		pathLimitLabel.setLayoutData(layout.create());
		pathLimitLabel.setText("Maximum found paths");

		pathLimit = new Spinner(pathLimitComposite, SWT.BORDER);
		pathLimit.setLayoutData(layout.create());
		pathLimit.setValues(getInt(PathSearchPreference.PATH_LIMIT), 1, 100000, 0, 1, 100);

		showPathLimitDialog = new Button(container, SWT.CHECK);
		showPathLimitDialog.setText("Show the \"There are too many paths for given search input...\" dialog.");
		showPathLimitDialog.setSelection(getBool(PathSearchPreference.SHOW_PATH_LIMIT_REACHED));

		showSearchProgressDialog = new Button(container, SWT.CHECK);
		showSearchProgressDialog.setText("Show dialog displaying search progress.");
		showSearchProgressDialog.setSelection(getBool(PathSearchPreference.SHOW_SEARCH_PROGRESS_DIALOG));

		return container;
	}

	@Override
	public boolean performOk() {
		if (pathLimit == null || pathLimit.isDisposed()) {
			return true;
		}
		preference.putInt(PathSearchPreference.PATH_LIMIT, pathLimit.getSelection());
		preference.putBoolean(PathSearchPreference.SHOW_PATH_LIMIT_REACHED, showPathLimitDialog.getSelection());
		preference.putBoolean(PathSearchPreference.SHOW_SEARCH_PROGRESS_DIALOG, showSearchProgressDialog.getSelection());
		try {
			preference.flush();
		} catch (BackingStoreException bse) {
			MessageDialog.openError(getShell(), "Cannot Store Preferences",
					getTitle() + " preferences cannot be stored. They will be applied, "
							+ "but may not be carried over to next application instance.");
		}
		return true;
	}

}

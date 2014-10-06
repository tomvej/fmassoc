package org.tomvej.fmassoc.plugin.simplepruningfinder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.osgi.service.prefs.BackingStoreException;
import org.tomvej.fmassoc.core.search.PathFinderProvider;

public class Part {
	private static final String KEY_LENGTH = "length", KEY_OPTIONAL = "optional", KEY_MN = "mn";

	@Inject
	private IEclipseContext context;
	@Inject
	@Preference("org.tomvej.fmassoc.plugin.simplepruningfinder")
	private IEclipsePreferences preference;

	private Spinner length;
	private Button optional, mn;

	@PostConstruct
	public void createComponents(Composite parent) {
		parent.setLayout(new RowLayout(SWT.VERTICAL));

		Composite lengthComposite = new Composite(parent, SWT.NONE);
		lengthComposite.setLayout(new GridLayout(2, false));
		GridDataFactory layout = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER);

		Label lengthLbl = new Label(lengthComposite, SWT.NONE);
		lengthLbl.setLayoutData(layout.create());
		lengthLbl.setText("Length limit");

		length = new Spinner(lengthComposite, SWT.BORDER);
		length.setLayoutData(layout.create());
		length.setValues(0 /* TODO selection */, 0, 20, 0, 1, 5);
		// TODO there is a problem with width

		optional = new Button(parent, SWT.CHECK);
		optional.setText("Optional associations");

		mn = new Button(parent, SWT.CHECK);
		mn.setText("M:N associations");

		length.setSelection(preference.getInt(KEY_LENGTH, 10));
		optional.setSelection(preference.getBoolean(KEY_OPTIONAL, false));
		mn.setSelection(preference.getBoolean(KEY_MN, false));
	}

	@PersistState
	public void savePreferences(Logger logger) {
		preference.putInt(KEY_LENGTH, length.getSelection());
		preference.putBoolean(KEY_OPTIONAL, optional.getSelection());
		preference.putBoolean(KEY_MN, mn.getSelection());
		try {
			preference.flush();
		} catch (BackingStoreException e) {
			logger.error(e, "Unable to store selected options.");
		}
	}

	private void refresh() {
		context.set(PathFinderProvider.class, new SimplePathFinderProvider(
				new Settings(optional.getSelection(), mn.getSelection(), length.getSelection(), 1)));
	}

}

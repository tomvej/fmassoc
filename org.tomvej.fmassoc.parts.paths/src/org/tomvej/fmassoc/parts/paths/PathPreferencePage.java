package org.tomvej.fmassoc.parts.paths;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.tomvej.fmassoc.core.preference.ContextPreferencePage;

public class PathPreferencePage extends PreferencePage implements ContextPreferencePage {
	private PathPreferenceManager manager;

	public PathPreferencePage() {
		super("Found paths table");
	}

	@Override
	public void init(IEclipseContext context) {
		manager = context.get(PathPreferenceManager.class);
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());

		return container;
	}
}

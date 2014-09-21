package org.tomvej.fmassoc.plugin.mobilemodelloader.wizards;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class ForbiddenPage extends WizardPage {

	public ForbiddenPage() {
		super("Forbidden Tables");
		setTitle("Forbidden Tables");
		setDescription("Specify default forbidden tables (search does not cross them, unless they are source or destination).");
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);

		// FIXME

		setControl(container);
	}
}

package org.tomvej.fmassoc.plugin.examplemodelloader;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

public class ExampleWizardPage extends WizardPage {

	public ExampleWizardPage(String title, String description) {
		super(title);
		setTitle(title);
		setDescription(description);
	}

	@Override
	public void createControl(Composite parent) {
		setControl(new Composite(parent, SWT.NONE));
	};

}

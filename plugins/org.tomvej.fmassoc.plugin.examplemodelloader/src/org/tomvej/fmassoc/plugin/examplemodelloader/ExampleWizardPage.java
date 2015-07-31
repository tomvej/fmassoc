package org.tomvej.fmassoc.plugin.examplemodelloader;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Wizard page for example model.
 * 
 * @author Tomáš Vejpustek
 */
public class ExampleWizardPage extends WizardPage {

	/**
	 * Specify title and description.
	 */
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

package org.tomvej.fmassoc.plugin.constantmodelloader;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * The only page for "new" wizard of constant data model.
 * 
 * @author vejpustekt
 *
 */
public class NewWizardPage extends WizardPage {
	NewWizardPage() {
		super("New constant data model");
		setTitle("New Constant Data Model");
		setDescription("Create new constant data model.");
	}

	@Override
	public void createControl(Composite parent) {
		setControl(parent);
	}
}
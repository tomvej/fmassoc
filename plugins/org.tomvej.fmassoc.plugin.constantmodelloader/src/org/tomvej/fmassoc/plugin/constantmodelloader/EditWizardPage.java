package org.tomvej.fmassoc.plugin.constantmodelloader;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/**
 * The only page of "edit" wizard of constant data model lodaer.
 * 
 * @author vejpustekt
 *
 */
public class EditWizardPage extends WizardPage {

	EditWizardPage() {
		super("Edit constant data model");
		setTitle("Edit Constant Data Model");
		setDescription("There are no settings to edit");
	}

	@Override
	public void createControl(Composite parent) {
		setControl(parent);
	}

}

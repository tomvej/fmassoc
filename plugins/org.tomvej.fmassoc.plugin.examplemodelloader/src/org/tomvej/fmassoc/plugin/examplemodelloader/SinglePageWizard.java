package org.tomvej.fmassoc.plugin.examplemodelloader;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

public class SinglePageWizard extends Wizard {
	private final IWizardPage page;

	public SinglePageWizard(IWizardPage page) {
		this.page = page;
	}

	@Override
	public void addPages() {
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}

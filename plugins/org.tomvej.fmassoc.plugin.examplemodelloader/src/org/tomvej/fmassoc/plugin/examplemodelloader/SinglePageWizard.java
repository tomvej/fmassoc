package org.tomvej.fmassoc.plugin.examplemodelloader;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * Wizard with single page.
 * 
 * @author Tomáš Vejpustek
 */
public class SinglePageWizard extends Wizard {
	private final IWizardPage page;

	/**
	 * Specify contained page.
	 */
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

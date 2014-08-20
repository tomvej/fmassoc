package org.tomvej.fmassoc.plugin.constantmodelloader;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

/**
 * A very simple wizard consisting of only one page and doing nothing.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class OnePageWizard extends Wizard {
	private final IWizardPage page;

	/**
	 * Specify wizard page.
	 */
	public OnePageWizard(IWizardPage page) {
		this.page = Validate.notNull(page);
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

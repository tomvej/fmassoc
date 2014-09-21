package org.tomvej.fmassoc.plugin.mobilemodelloader.wizards;

import org.eclipse.jface.wizard.Wizard;

public class MobileModelWizard extends Wizard {

	@Override
	public String getWindowTitle() {
		return "Field Manager Model";
	}

	@Override
	public void addPages() {
		addPage(new FilePage());
		addPage(new ForbiddenPage());


	}

	@Override
	public boolean performFinish() {
		System.out.println("Perform finish.");
		// TODO Auto-generated method stub
		return false;
	}

}

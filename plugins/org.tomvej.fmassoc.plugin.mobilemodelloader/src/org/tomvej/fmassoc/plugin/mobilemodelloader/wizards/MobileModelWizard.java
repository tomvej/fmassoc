package org.tomvej.fmassoc.plugin.mobilemodelloader.wizards;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;
import org.tomvej.fmassoc.plugin.mobilemodelloader.xml.DataModelNode;

public class MobileModelWizard extends Wizard {
	private FilePage file;
	private ForbiddenPage forbidden;

	@Override
	public String getWindowTitle() {
		return "Field Manager Model";
	}

	@Override
	public void addPages() {
		addPage(file = new FilePage());
		addPage(forbidden = new ForbiddenPage());
	}

	@Override
	public void createPageControls(Composite pageContainer) {
		super.createPageControls(pageContainer);
		// add listener which loads model so that forbidden tables can be chosen
		if (getContainer() instanceof WizardDialog) {
			((WizardDialog) getContainer()).addPageChangingListener(this::pageChanging);
		} else {
			// if this does not work, forbidden table cannot be chosen
			forbidden = null;
		}
	}

	private void pageChanging(PageChangingEvent event) {
		if (event.getCurrentPage().equals(file) && event.getTargetPage().equals(forbidden)) {
			try {
				Unmarshaller unmarshaller = JAXBContext.newInstance(DataModelNode.class).createUnmarshaller();
				DataModelNode model = (DataModelNode) unmarshaller.unmarshal(new File(file.getFile()));
				forbidden.setTables(model.transform().create().getTables());
			} catch (JAXBException | ModelLoadingException e) {
				event.doit = false;
				file.setErrorMessage("Model could not be loaded.");
				file.setPageComplete(false);
				// needs separate field for error message (top too short)
			}
		}
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		return forbidden;
	}

	@Override
	public boolean performFinish() {
		System.out.println("Perform finish.");
		// TODO Auto-generated method stub
		return false;
	}

}

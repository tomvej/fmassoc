package org.tomvej.fmassoc.plugin.mobilemodelloader.wizards;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;
import org.tomvej.fmassoc.plugin.mobilemodelloader.MobileModelLoader;

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
			BusyIndicator.showWhile(getShell().getDisplay(), () -> {
				try {
					Collection<Table> tables = MobileModelLoader.loadModel(new File(file.getFile())).getTables();
					forbidden.setTables(tables);
					Collection<Table> defForbid = tables.stream()
							.filter(t -> getDefaultForbiddenNames().contains(t.getName()))
							.collect(Collectors.toList());
					forbidden.setForbidden(defForbid);
				} catch (JAXBException | ModelLoadingException e) {
					event.doit = false;
					file.setErrorMessage("Model could not be loaded.");
					file.setPageComplete(false);
					file.setException(e);
				}
			});
		}
	}

	private Collection<String> getDefaultForbiddenNames() {
		return Arrays.asList("CREW", "BLOB", "ASSIGNMENT");
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		return forbidden;
	}

	@Override
	public boolean performFinish() {
		// FIXME configuration
		return false;
	}

}

package org.tomvej.fmassoc.plugin.mobilemodelloader.wizards;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Composite;
import org.osgi.service.prefs.BackingStoreException;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;
import org.tomvej.fmassoc.plugin.mobilemodelloader.MobileModelLoader;
import org.tomvej.fmassoc.plugin.mobilemodelloader.ModelStorage;

/**
 * Wizard for field manager data model management.
 * 
 * @author Tomáš Vejpustek
 */
public class MobileModelWizard extends Wizard {
	private FilePage file;
	private ForbiddenPage forbidden;
	private final ModelStorage preference;
	private final boolean edit;

	/**
	 * Specify model.
	 * 
	 * @param modelId
	 *            Unique identifier of the loaded model.
	 * @param edit
	 *            {@code true} when model exists and is being edited,
	 *            {@code false} when a new model is created (configured).
	 */
	public MobileModelWizard(String modelId, boolean edit) {
		preference = new ModelStorage(modelId);
		this.edit = edit;
	}

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

		if (edit) {
			if (preference.getFile() == null) {
				file.setErrorMessage("Cannot read stored preference.");
			} else {
				file.setFile(preference.getFile());
			}
		}
	}

	private void pageChanging(PageChangingEvent event) {
		if (event.getCurrentPage().equals(file) && event.getTargetPage().equals(forbidden)) {
			BusyIndicator.showWhile(getShell().getDisplay(), () -> {
				try {
					Collection<Table> tables = MobileModelLoader.loadModel(new File(file.getFile())).getTables();
					forbidden.setTables(tables);
					Collection<Table> forbiddenTables = null;

					if (edit) {
						try {
							Collection<String> forbidNames = preference.getForbidden().stream().collect(Collectors.toSet());
							forbiddenTables = tables.stream().
									filter(t -> forbidNames.contains(t.getName())).collect(Collectors.toList());
						} catch (BackingStoreException bse) {
							throw new ModelLoadingException("Cannot read stored model preferences.", bse);
						}
					} else {
						forbiddenTables = tables.stream().
								filter(t -> getDefaultForbiddenNames().contains(t.getName()))
								.collect(Collectors.toList());
					}
					forbidden.setForbidden(forbiddenTables);
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
		return Arrays.asList("CREW", "BLOB", "ASSIGNMENT").stream().collect(Collectors.toSet());
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		return forbidden;
	}

	@Override
	public boolean performFinish() {
		try {
			preference.setFile(file.getFile());
			if (forbidden != null) {
				preference.setForbiddenTables(forbidden.getForbidden());
			}
			preference.store();
			return true;
		} catch (BackingStoreException bse) {
			MessageDialog.openError(getShell(), "Cannot store model", "Unable to store model: " + bse.getLocalizedMessage());
		}
		return false;
	}
}

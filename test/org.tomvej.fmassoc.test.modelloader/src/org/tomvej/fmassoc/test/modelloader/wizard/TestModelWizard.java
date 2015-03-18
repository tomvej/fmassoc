package org.tomvej.fmassoc.test.modelloader.wizard;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.osgi.service.prefs.BackingStoreException;
import org.tomvej.fmassoc.test.modelloader.ModelStorage;

/**
 * Wizard for test model.
 * 
 * @author Tomáš Vejpustek
 */
public class TestModelWizard extends Wizard {
	private final ModelStorage preference;
	private SettingsPage page;

	/**
	 * Specify model id.
	 */
	public TestModelWizard(String id) {
		preference = new ModelStorage(id);
	}

	@Override
	public void addPages() {
		addPage(page = new SettingsPage(preference));
	}

	@Override
	public boolean performFinish() {
		try {
			page.store();
			return true;
		} catch (BackingStoreException e) {
			MessageDialog.openError(getShell(), "Cannot store model", "Unable to store model: " + e.getLocalizedMessage());
			return false;
		}
	}
}

package org.tomvej.fmassoc.test.modelloader.wizard;

import java.util.Objects;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;
import org.tomvej.fmassoc.swt.wrappers.VerifyWrapper;
import org.tomvej.fmassoc.test.modelloader.ModelStorage;

/**
 * Allows to specify settings for testing model -- see {@link ModelStorage}.
 * 
 * @author Tomáš Vejpustek
 */
public class SettingsPage extends WizardPage {
	private final ModelStorage preference;
	private Text time;
	private Button fail;

	/**
	 * Specify preferences.
	 */
	public SettingsPage(ModelStorage preference) {
		super("Testing Model Settings");
		setTitle("Testing Model Settings");
		setDescription("Specify preferences for testing model loader.");

		this.preference = Objects.requireNonNull(preference);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		Label timeLbl = new Label(container, SWT.NONE);
		timeLbl.setText("Loads in ms (approximately):");

		time = new Text(container, SWT.BORDER);
		time.setLayoutData(GridDataFactory.fillDefaults().hint(100, SWT.DEFAULT).create());
		time.setText(Long.toString(preference.getDuration()));
		time.addVerifyListener(new VerifyWrapper(this::verifyTime));

		fail = new Button(container, SWT.CHECK);
		fail.setText("Model loading will fail.");
		fail.setSelection(preference.fails());
		setControl(container);
	}

	private boolean verifyTime(String newText) {
		if (newText.isEmpty()) {
			return true;
		}

		try {
			Long t = Long.valueOf(newText);
			return t >= 0;
		} catch (NumberFormatException nfe) {
			return false;
		}
	}

	/**
	 * Attempt to store preferences specified by this page.
	 * 
	 * @throws BackingStoreException
	 */
	public void store() throws BackingStoreException {
		String timeText = time.getText();
		preference.setDuration(timeText.isEmpty() ? 0 : Long.valueOf(time.getText()));
		preference.setFails(fail.getSelection());
		preference.store();
	}

}

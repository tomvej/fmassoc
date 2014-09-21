package org.tomvej.fmassoc.plugin.mobilemodelloader.wizards;

import java.io.File;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;

public class FilePage extends WizardPage {
	private FileDialog dialog;
	private Text input;

	public FilePage() {
		super("Model file");
		setTitle("Model File");
		setDescription("Select the `model.xml' file containing Field Manager data model.");
	}

	private void createDialog() {
		dialog = new FileDialog(getShell(), SWT.OPEN | SWT.SINGLE);
		dialog.setFilterNames(new String[] { "XML files (*.xml)" });
		dialog.setFilterExtensions(new String[] { "*.xml" });
	}

	@Override
	public void createControl(Composite parent) {
		createDialog();
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));


		input = new Text(container, SWT.BORDER);
		input.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		input.addModifyListener(e -> setPageComplete(fileExists()));

		Button btn = new Button(container, SWT.PUSH);
		btn.setLayoutData(GridDataFactory.fillDefaults().create());
		btn.setText("Browse...");
		btn.addSelectionListener(new SelectionWrapper(e -> openDialog()));
		// FIXME

		setControl(container);
		setPageComplete(false);
	}

	private boolean fileExists() {
		setErrorMessage(null);
		String text = input.getText();
		if (text.isEmpty()) {
			return false;
		}
		if (new File(text).exists()) {
			return true;
		}
		setErrorMessage("File `" + input.getText() + "' does not exist.");
		return false;
	}

	private void openDialog() {
		dialog.open();
		if (dialog.getFileName() != null) {
			input.setText(dialog.getFilterPath() + File.separatorChar + dialog.getFileName());
		} else {
			input.setText("");
		}
	}


	public String getFile() {
		return input.getText();
	}
}

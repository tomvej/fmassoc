package org.tomvej.fmassoc.parts.altsrcdst.preference;

import javax.inject.Inject;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

public class PreferencePage extends org.eclipse.jface.preference.PreferencePage {
	@Inject
	private PreferenceManager manager;
	private ComboViewer displayInPopup;

	public PreferencePage() {
		super("Source and destination");
		noDefaultAndApplyButton();
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		Label lbl = new Label(container, SWT.NONE);
		lbl.setText("Display");

		displayInPopup = new ComboViewer(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		displayInPopup.setContentProvider(ArrayContentProvider.getInstance());
		displayInPopup.setInput(PopupDisplayProperty.values());
		displayInPopup.setSelection(new StructuredSelection(manager.getDisplayProperty()));

		return container;
	}

	@Override
	public boolean performOk() {
		if (displayInPopup == null || displayInPopup.getCombo().isDisposed()) {
			return true;
		}
		manager.setDisplayProperty((PopupDisplayProperty)
				((IStructuredSelection) displayInPopup.getSelection()).getFirstElement());
		return true;
	}
}

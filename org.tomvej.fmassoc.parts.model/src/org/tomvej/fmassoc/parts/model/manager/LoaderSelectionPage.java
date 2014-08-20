package org.tomvej.fmassoc.parts.model.manager;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.core.wrappers.TextLabelProvider;
import org.tomvej.fmassoc.parts.model.core.ModelLoaderEntry;

public class LoaderSelectionPage extends WizardSelectionPage {
	private Text name, description;
	private ListViewer loaders;
	private final List<ModelLoaderEntry> loaderRegistry;

	public LoaderSelectionPage(List<ModelLoaderEntry> loaders) {
		super("New Model");
		setTitle("New Model");
		setDescription("Select model name and type");
		this.loaderRegistry = Validate.notNull(loaders);
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(1, false));

		Label label = new Label(container, SWT.NONE);
		label.setText("Model name:");

		name = new Text(container, SWT.BORDER | SWT.SINGLE);
		name.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		label = new Label(container, SWT.NONE);
		label.setText("Model type:");

		loaders = new ListViewer(container, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL);
		loaders.getList().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());

		// 3 lines by default
		description = new Text(container, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.BORDER);
		description.setLayoutData(GridDataFactory.fillDefaults().hint(0, 3 * description.getLineHeight()).create());

		name.addModifyListener(e -> refresh());

		loaders.setLabelProvider(new TextLabelProvider<ModelLoaderEntry>(e -> e.getName()));
		loaders.addSelectionChangedListener(e -> loaderSelected());
		loaders.setContentProvider(ArrayContentProvider.getInstance());
		loaders.setInput(loaderRegistry);

		setControl(container);
	}

	private void loaderSelected() {
		ModelLoaderEntry selected = (ModelLoaderEntry) ((StructuredSelection) loaders.getSelection()).getFirstElement();
		if (selected != null) {
			description.setText(selected.getDescription());
		}
		refresh();
	}

	private void refresh() {

	}
}

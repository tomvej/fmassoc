package org.tomvej.fmassoc.parts.model.manager;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardNode;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardSelectionPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.core.wrappers.TextLabelProvider;
import org.tomvej.fmassoc.parts.model.core.ModelEntry;
import org.tomvej.fmassoc.parts.model.core.ModelList;
import org.tomvej.fmassoc.parts.model.core.ModelLoaderEntry;

class LoaderSelectionPage extends WizardSelectionPage implements IWizardNode {
	private final Shell parentShell;
	private Text name, description;
	private ListViewer loaders;
	private final List<ModelLoaderEntry> loaderRegistry;
	private final ModelList models;
	private IWizard currentWizard;
	private ModelEntry currentModel;

	LoaderSelectionPage(Shell parent, List<ModelLoaderEntry> loaders, ModelList models) {
		super("New Model");
		setTitle("New Model");
		setDescription("Select model name and type");
		this.loaderRegistry = Validate.notNull(loaders);
		this.parentShell = Validate.notNull(parent);
		this.models = Validate.notNull(models);
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
		loaders.addSelectionChangedListener(e -> refresh());
		loaders.setContentProvider(ArrayContentProvider.getInstance());
		loaders.setInput(loaderRegistry);

		setControl(container);
	}

	private ModelLoaderEntry getSelected() {
		return (ModelLoaderEntry) ((StructuredSelection) loaders.getSelection()).getFirstElement();
	}

	private void refresh() {
		boolean selected = !loaders.getSelection().isEmpty();
		description.setText(selected ? getSelected().getDescription() : "");
		if (selected && StringUtils.isNotBlank(name.getText())) {
			setSelectedNode(this);
		} else {
			setSelectedNode(null);
		}
	}

	private void removeCurrentModel() {
		if (currentModel != null) {
			models.remove(currentModel);
			currentModel = null;
		}
	}

	// IWizardNode methods
	@Override
	public boolean isContentCreated() {
		return currentWizard != null;
	}

	@Override
	public void dispose() {
		currentWizard = null;
	}

	@Override
	public IWizard getWizard() {
		if (currentWizard == null) {
			currentModel = models.add(name.getText(), getSelected());
			if (currentModel != null) {
				currentWizard = currentModel.getLoader().getLoader().createNewWizard(currentModel.getId());
			} else {
				MessageDialog.openError(parentShell, "Cannot create model", "The model " + currentModel.getLabel()
						+ " (" + currentModel.getLoader().getName() + ") could not be created.");
				// FIXME close the dialog
			}
		}
		return currentWizard;
	}

	@Override
	public Point getExtent() {
		return new Point(-1, -1);
	}


	// WizardDialog
	private class InnerWizard extends Wizard {
		public InnerWizard() {
			setForcePreviousAndNextButtons(true);
		}

		@Override
		public void addPages() {
			addPage(LoaderSelectionPage.this);
		}

		@Override
		public boolean performFinish() {
			return true;
		}
	}

	private class Dialog extends WizardDialog {
		public Dialog() {
			super(parentShell, new InnerWizard());
			addPageChangingListener(e -> {
				if (LoaderSelectionPage.this.equals(e.getTargetPage())) {
					removeCurrentModel();
				}
			});
		}

		@Override
		protected void cancelPressed() {
			removeCurrentModel();
			super.cancelPressed();
		}
	}

	public WizardDialog getNewModelDialog() {
		return new Dialog();
	}
}

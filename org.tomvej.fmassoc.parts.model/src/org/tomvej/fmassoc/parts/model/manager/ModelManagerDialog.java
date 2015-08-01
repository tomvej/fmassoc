package org.tomvej.fmassoc.parts.model.manager;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;
import org.tomvej.fmassoc.parts.model.core.Constants;
import org.tomvej.fmassoc.parts.model.core.ModelEntry;
import org.tomvej.fmassoc.parts.model.core.ModelList;
import org.tomvej.fmassoc.parts.model.core.ModelLoaderEntry;
import org.tomvej.fmassoc.swt.tables.TableLayoutSupport;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;

/**
 * Dialog used to manage (add, edit, remove) available data models. Uses
 * dependency injection -- must be created from context.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class ModelManagerDialog extends TitleAreaDialog {
	@Inject
	private ModelList models;
	@Inject
	@Named(Constants.MODEL_LOADER_REGISTRY)
	private List<ModelLoaderEntry> loaders;
	@Inject
	@Optional
	private ModelEntry current;

	@Inject
	@Named(Constants.MODEL_ERRORS)
	private Map<ModelEntry, ModelLoadingException> errors;

	private TableViewer list;
	private Button editBtn, removeBtn;
	private boolean currentChanged;

	/**
	 * Create model manager dialog. Not to be used explicitly, only from
	 * dependency injection.
	 */
	@Inject
	public ModelManagerDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialog = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(dialog, SWT.NONE);
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		container.setLayout(new GridLayout(2, false));

		list = TableLayoutSupport.createTableViewer(container, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION,
				GridDataFactory.fillDefaults().grab(true, true).span(1, 3).create());
		TableViewerColumn model = new TableViewerColumn(list, SWT.LEFT);
		model.setLabelProvider(new ModelLabelProvider(errors));
		TableLayoutSupport.create(list, 1, false, model);
		ColumnViewerToolTipSupport.enableFor(list, ToolTip.NO_RECREATE);

		list.setContentProvider(new ObservableListContentProvider());
		list.setInput(models);
		list.addSelectionChangedListener(e -> refreshButtons());

		createButton(container, "Add",
				e -> new LoaderSelectionPage(getParentShell(), loaders, models).getNewModelDialog().open());
		editBtn = createButton(container, "Edit",
				e -> new WizardDialogWithCheck(getParentShell(), getSelected().createEditWizard()).open());
		removeBtn = createButton(container, "Remove",
				e -> models.remove(list.getTable().getSelectionIndex()));
		refreshButtons();
		return dialog;
	}

	private Button createButton(Composite parent, String title, Consumer<SelectionEvent> action) {
		Button result = new Button(parent, SWT.PUSH);
		result.setText(title);
		result.addSelectionListener(new SelectionWrapper(action));
		result.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		return result;
	}

	private void refreshButtons() {
		boolean selected = !list.getSelection().isEmpty();
		editBtn.setEnabled(selected && getSelected().isValid());
		removeBtn.setEnabled(selected);
	}

	private ModelEntry getSelected() {
		return (ModelEntry) ((StructuredSelection) list.getSelection()).getFirstElement();
	}

	@Override
	public void create() {
		super.create();
		setTitle("Data model management");
		setMessage("Add, remove and configure available data models.");
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Models");
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
	}

	/**
	 * Return whether the currently loaded model has been edited in manager.
	 * This only checks if "Finish" has been pressed in its edit dialog.
	 */
	public boolean isCurrentModelChanged() {
		return currentChanged;
	}

	private class WizardDialogWithCheck extends WizardDialog {

		public WizardDialogWithCheck(Shell parentShell, IWizard newWizard) {
			super(parentShell, newWizard);
		}

		@Override
		protected void finishPressed() {
			ModelEntry selected = getSelected();
			if (selected.equals(current)) {
				currentChanged = true;
			}
			// remove errors for edited model
			errors.remove(selected);
			list.refresh(selected);
			super.finishPressed();
		}
	}


}

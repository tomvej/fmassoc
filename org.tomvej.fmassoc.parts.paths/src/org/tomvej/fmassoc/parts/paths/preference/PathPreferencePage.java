package org.tomvej.fmassoc.parts.paths.preference;

import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.preference.ContextPreferencePage;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;
import org.tomvej.fmassoc.swt.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.swt.wrappers.TextLabelProvider;

/**
 * Preference page for found paths table. Sets label provider and visible
 * columns.
 * 
 * @author Tomáš Vejpustek
 */
public class PathPreferencePage extends PreferencePage implements ContextPreferencePage {
	private PathPreferenceManager manager;
	private List<PathPropertyEntry<?>> properties;
	private ComboViewer provider;
	private Text providerDescription;
	private CheckboxTableViewer columns;

	/**
	 * Create this preference page.
	 */
	public PathPreferencePage() {
		super("Found paths table");
	}

	@SuppressWarnings("unchecked")
	@Override
	public void init(IEclipseContext context) {
		manager = context.get(PathPreferenceManager.class);
		properties = (List<PathPropertyEntry<?>>) context.get(ContextObjects.PATH_PROPERTIES);
		noDefaultAndApplyButton();
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		// Path column label provider

		Label lbl = new Label(container, SWT.NONE);
		lbl.setText("Path format:");

		provider = new ComboViewer(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		provider.setLabelProvider(new TextLabelProvider<LabelProviderEntry>(e -> e.getName()));
		provider.setContentProvider(ArrayContentProvider.getInstance());
		provider.setInput(manager.getLabelProviders());

		providerDescription = new Text(container, SWT.READ_ONLY | SWT.MULTI | SWT.WRAP | SWT.BORDER);
		providerDescription.setLayoutData(GridDataFactory.fillDefaults().grab(true, false)
				.hint(0, 3 * providerDescription.getLineHeight()).span(2, 1).create());

		LabelProviderEntry selected = manager.getLabelProviderEntry();
		if (selected != null) {
			provider.setSelection(new StructuredSelection(selected));
			setProviderDescription(selected);
		}
		provider.addSelectionChangedListener(e -> setProviderDescription(getSelectedEntry()));

		lbl = new Label(container, SWT.NONE);
		lbl.setText("Displayed columns:");
		lbl.setLayoutData(GridDataFactory.fillDefaults().span(2, 1).create());

		// Properties columns
		columns = CheckboxTableViewer.newCheckList(container, SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		columns.getTable().setLayoutData(GridDataFactory.fillDefaults().span(2, 1).grab(true, false).create());
		columns.getTable().setLinesVisible(true);
		columns.getTable().setHeaderVisible(true);

		TableViewerColumn checkColumn = new TableViewerColumn(columns, SWT.CENTER);
		checkColumn.setLabelProvider(new TextColumnLabelProvider<PathPropertyEntry<?>>(p -> ""));

		TableViewerColumn nameColumn = new TableViewerColumn(columns, SWT.LEFT);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new TextColumnLabelProvider<PathPropertyEntry<?>>(e -> e.getName()));

		TableViewerColumn descriptionColumn = new TableViewerColumn(columns, SWT.LEFT);
		descriptionColumn.getColumn().setText("Description");
		descriptionColumn.setLabelProvider(new TextColumnLabelProvider<PathPropertyEntry<?>>(e -> e.getDescription()));

		columns.setContentProvider(ArrayContentProvider.getInstance());
		columns.setInput(properties);
		columns.setCheckedElements(manager.getColumns().toArray());
		nameColumn.getColumn().pack();
		descriptionColumn.getColumn().pack();
		checkColumn.getColumn().setWidth(30);
		checkColumn.getColumn().setResizable(false);

		return container;
	}

	private void setProviderDescription(LabelProviderEntry entry) {
		String description = "";
		if (entry != null && entry.getDescription() != null) {
			description = entry.getDescription();
		}
		providerDescription.setText(description);
	}

	private LabelProviderEntry getSelectedEntry() {
		return (LabelProviderEntry) ((IStructuredSelection) provider.getSelection()).getFirstElement();
	}

	@Override
	public boolean performOk() {
		if (provider == null || provider.getCombo().isDisposed()) {
			return true;
		}
		manager.setLabelProvider(getSelectedEntry());
		for (PathPropertyEntry<?> entry : properties) {
			if (columns.getChecked(entry)) {
				manager.addColumn(entry);
			} else {
				manager.removeColumn(entry);
			}
		}
		if (!manager.store()) {
			MessageDialog.openError(getShell(), "Cannot Store Preferences",
					getTitle() + " preferences could not be stored. They will be applied,"
							+ " but may not be carried over to next application instance.");
		}
		return true;
	}
}

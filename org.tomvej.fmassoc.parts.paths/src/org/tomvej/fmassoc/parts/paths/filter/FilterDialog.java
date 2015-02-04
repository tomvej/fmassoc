package org.tomvej.fmassoc.parts.paths.filter;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.core.wrappers.TextLabelProvider;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.parts.paths.filterprovider.FilterProvider;

/**
 * Dialog for path filtering.
 * 
 * @author Tomáš Vejpustek
 */
public class FilterDialog extends Dialog {
	private Composite panel;
	private ComboViewer availableFilters;
	private Button addBtn;

	private Collection<FilterInstance<?>> persistedFilters = Collections.emptyList();
	private Map<FilterInstance<?>, Composite> currentFilters = new HashMap<>();

	private Map<PathPropertyEntry<?>, FilterProvider<?>> providers = Collections.emptyMap();

	/**
	 * Initialize dialog.
	 * 
	 * @param parent
	 */
	public FilterDialog(Shell parent) {
		super(parent);
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Path Filter");
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final int width = 3;
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		container.setLayout(new GridLayout(width, false));

		panel = new Composite(container, SWT.NONE);
		panel.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(width, 1).create());
		panel.setLayout(new GridLayout());

		availableFilters = new ComboViewer(container, SWT.DROP_DOWN | SWT.READ_ONLY);
		availableFilters.setContentProvider(ArrayContentProvider.getInstance());
		availableFilters.setLabelProvider(new TextLabelProvider<PathPropertyEntry<?>>(e -> e.getName()));
		availableFilters.setInput(providers.keySet());
		availableFilters.addSelectionChangedListener(e -> addBtn.setEnabled(!e.getSelection().isEmpty()));

		persistedFilters.forEach(this::addFilter);

		addBtn = new Button(container, SWT.PUSH);
		addBtn.setText("Add filter");
		addBtn.addSelectionListener(new SelectionWrapper(e -> addSelectedFilter()));
		addBtn.setEnabled(false);

		Button clearFilterBtn = new Button(container, SWT.PUSH);
		clearFilterBtn.setText("Clear");
		clearFilterBtn.addSelectionListener(new SelectionWrapper(e -> clearFilter()));

		return container;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void addSelectedFilter() {
		PathPropertyEntry<?> entry = (PathPropertyEntry<?>) ((IStructuredSelection) availableFilters.getSelection())
				.getFirstElement();
		addFilter(new FilterInstance(entry, providers.get(entry).get()));
		refresh();
	}

	private void addFilter(FilterInstance<?> instance) {
		currentFilters.put(instance, instance.createFilterPanel(panel, this::refresh));
	}

	private void refresh() {
		Shell shell = getShell();
		if (shell != null) {
			shell.pack();
		}
	}

	private void clearFilter() {
		currentFilters.values().forEach(f -> f.dispose());
		currentFilters.clear();
		refresh();
	}

	@Override
	protected void okPressed() {
		currentFilters.entrySet().removeIf(e -> e.getValue().isDisposed());
		persistedFilters = currentFilters.keySet();
		currentFilters = new HashMap<>();
		super.okPressed();
	}

	@Override
	protected void cancelPressed() {
		currentFilters = new HashMap<>();
		super.cancelPressed();
	}

	/**
	 * Set which properties can be filtered.
	 */
	public void setColumns(Map<PathPropertyEntry<?>, FilterProvider<?>> providers) {
		this.providers = Validate.notNull(providers);
		if (availableFilters != null && !availableFilters.getCombo().isDisposed()) {
			availableFilters.setInput(providers.keySet());
		}
		clearFilter();
	}

	/**
	 * Return actual filter.
	 */
	public Predicate<Path> getFilter() {
		return persistedFilters.isEmpty() ? null : new CompoundFilter(persistedFilters);
	}
}

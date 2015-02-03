package org.tomvej.fmassoc.parts.paths.filter;

import java.util.Collections;
import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;
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

		return container;
	}

	/**
	 * Set which properties can be filtered.
	 */
	public void setColumns(Map<PathPropertyEntry<?>, FilterProvider<?>> providers) {
		this.providers = Validate.notNull(providers);
		if (availableFilters != null && !availableFilters.getCombo().isDisposed()) {
			availableFilters.setInput(providers.keySet());
		}
		// FIXME clear filters
	}

	/**
	 * Return actual filter.
	 */
	public Predicate<Path> getFilter() {
		// FIXME
		return null;
	}
}

package org.tomvej.fmassoc.plugin.filterpruningfinder;

import java.util.Map;
import java.util.function.Predicate;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.tomvej.fmassoc.core.dnd.CompositeDnDSupport;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.filter.FilterProvider;
import org.tomvej.fmassoc.filter.dialog.FilterDialog;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.plugin.prioritydfpathfinder.Pruning;
import org.tomvej.fmassoc.plugin.prioritydfpathfinder.PruningWrapper;

public class PruningRow extends Composite {
	private FilterDialog dialog;
	private Label filterLbl;
	private Runnable listener;

	public PruningRow(Composite parent, Map<PathPropertyEntry<?>, FilterProvider<?>> providers, boolean removable,
			Runnable pruningChangeListener) {
		super(parent, SWT.BORDER);
		listener = Validate.notNull(pruningChangeListener);

		GridDataFactory grabLayout = GridDataFactory.fillDefaults().grab(true, false);
		setLayoutData(grabLayout.create());
		setLayout(new GridLayout(3, false));

		dialog = new FilterDialog(getShell());
		dialog.setColumns(providers);

		filterLbl = new Label(this, SWT.WRAP);
		filterLbl.setLayoutData(grabLayout.create());

		GridDataFactory btnLayout = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.TOP);

		Button filterBtn = new Button(this, SWT.PUSH);
		filterBtn.setText("...");
		filterBtn.setLayoutData(btnLayout.create());
		filterBtn.addSelectionListener(new SelectionWrapper(e -> showFilter()));

		if (removable) {
			Button rmBtn = new Button(this, SWT.PUSH);
			rmBtn.setText("X");
			rmBtn.setLayoutData(btnLayout.create());
			rmBtn.addSelectionListener(new SelectionWrapper(e -> {
				dispose();
				parent.getParent().layout();
			}));
		}
	}

	private void showFilter() {
		dialog.open();
		if (dialog.getReturnCode() == Dialog.OK) { // over-approximation
			listener.run();
			filterLbl.setText(dialog.getFilter() != null ? dialog.getFilter().toString() : "");
			getParent().layout(); // this one is for base pruning
			getParent().getParent().layout();
		}
	}

	public Pruning getPruning() {
		return dialog.getPruning() != null ? new PruningWrapper(dialog.getPruning(), dialog.getProperties()) : null;
	}

	public void pluginDnD(CompositeDnDSupport support) {
		support.registerKnob(filterLbl, this);
		support.registerKnob(this, this);
	}

	@Override
	public String toString() {
		Predicate<Path> filter = dialog.getFilter();
		if (filter != null) {
			return "[" + filter + "]";
		} else {
			return "none";
		}
	}

}

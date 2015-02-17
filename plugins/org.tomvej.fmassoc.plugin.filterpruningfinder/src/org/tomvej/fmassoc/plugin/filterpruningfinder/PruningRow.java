package org.tomvej.fmassoc.plugin.filterpruningfinder;

import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

import org.apache.commons.lang3.Validate;
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
		Predicate<Path> oldFilter = dialog.getFilter();
		dialog.open();
		Predicate<Path> newFilter = dialog.getFilter();
		if (Objects.equals(oldFilter, dialog.getFilter())) {
			listener.run();
			filterLbl.setText(newFilter != null ? newFilter.toString() : "");
			getParent().layout(); // this one is for base pruning
			getParent().getParent().layout();
		}
	}

	public Pruning getPruning() {
		return null;
	}

	public void pluginDnD(CompositeDnDSupport support) {
		support.registerKnob(filterLbl, this);
		support.registerKnob(this, this);
	}


}

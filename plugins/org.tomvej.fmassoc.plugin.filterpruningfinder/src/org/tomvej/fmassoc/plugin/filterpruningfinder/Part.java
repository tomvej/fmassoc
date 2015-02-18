package org.tomvej.fmassoc.plugin.filterpruningfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.dnd.CompositeDnDSupport;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.filter.FilterProvider;
import org.tomvej.fmassoc.filter.FilterRegistry;

public class Part {
	@Inject
	private Logger logger;
	private final Map<PathPropertyEntry<?>, FilterProvider<?>> providers;

	private Composite pruningPanel;
	private PruningRow basePruning;
	private List<PruningRow> pruning;
	private CompositeDnDSupport dndSupport;

	/**
	 * Initialize filter providers.
	 */
	@Inject
	public Part(@Named(ContextObjects.PATH_PROPERTIES) List<PathPropertyEntry<?>> pathProperties,
			FilterRegistry filters) {
		Map<PathPropertyEntry<?>, FilterProvider<?>> providers = new HashMap<>();
		for (PathPropertyEntry<?> property : pathProperties) {
			FilterProvider<?> provider = filters.apply(property.getProperty().getType());
			if (provider != null) {
				providers.put(property, provider);
			} else {
				logger.warn("No filter for " + property.getName() + ".");
			}
		}
		this.providers = Collections.unmodifiableMap(providers);
	}

	/**
	 * Create visual components.
	 */
	@PostConstruct
	public void createComponents(Composite parent) {
		// generate component
		parent.setLayout(new GridLayout(2, false));

		GridDataFactory twoColumnLayout = GridDataFactory.fillDefaults().span(2, 1).grab(true, false);

		Label lbl = new Label(parent, SWT.NONE);
		lbl.setText("Persistent Filter");
		lbl.setLayoutData(twoColumnLayout.create());

		basePruning = new PruningRow(parent, providers, false, this::fireFilterChanged);
		basePruning.setLayoutData(twoColumnLayout.create());

		lbl = new Label(parent, SWT.NONE);
		lbl.setText("Filter Iterations");
		lbl.setLayoutData(twoColumnLayout.create());

		pruningPanel = new Composite(parent, SWT.NONE);
		pruningPanel.setLayoutData(twoColumnLayout.create());
		GridLayout pruningPanelLayout = new GridLayout();
		pruningPanelLayout.marginWidth = 0;
		pruningPanelLayout.marginHeight = 0;
		pruningPanel.setLayout(pruningPanelLayout);

		Button addBtn = new Button(parent, SWT.PUSH);
		addBtn.setLayoutData(GridDataFactory.fillDefaults().align(SWT.BEGINNING, SWT.FILL).create());
		addBtn.setText("Add Filter");
		addBtn.addSelectionListener(new SelectionWrapper(e -> addRow()));

		Button clearBtn = new Button(parent, SWT.PUSH);
		clearBtn.setLayoutData(GridDataFactory.fillDefaults().align(SWT.END, SWT.FILL).create());
		clearBtn.setText("Remove All");
		clearBtn.addSelectionListener(new SelectionWrapper(e -> clearRows()));

		// initialize reordering
		dndSupport = new CompositeDnDSupport(pruningPanel);
		pruning = new ArrayList<>();
	}

	private void addRow() {
		PruningRow newRow = new PruningRow(pruningPanel, providers, true, this::fireFilterChanged);
		pruning.add(newRow);
		newRow.pluginDnD(dndSupport);
		pruningPanel.getParent().layout();
	}

	private void clearRows() {
		pruning.forEach(p -> p.dispose());
		pruning.clear();
		pruningPanel.getParent().layout();
	}

	private void fireFilterChanged() {
		pruning.removeIf(f -> f.isDisposed()); // clear up

	}

}

package org.tomvej.fmassoc.plugin.filterpruningfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;
import org.tomvej.fmassoc.core.search.PathFinderProvider;
import org.tomvej.fmassoc.filter.FilterProvider;
import org.tomvej.fmassoc.filter.FilterRegistry;
import org.tomvej.fmassoc.plugin.prioritydfpathfinder.IteratedPriorityDFFinderProvider;
import org.tomvej.fmassoc.plugin.prioritydfpathfinder.Pruning;
import org.tomvej.fmassoc.plugin.prioritydfpathfinder.PruningWrapper;
import org.tomvej.fmassoc.swt.dnd.CompositeDnDSupport;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;

/**
 * Visual component of filter pruning.
 * 
 * @author Tomáš Vejpustek
 */
public class Part {
	@Inject
	private Logger logger;
	private final Map<PathPropertyEntry<?>, FilterProvider<?>> providers;
	private final IEclipseContext context;

	private Composite pruningPanel;
	private PruningRow basePruning;
	private List<PruningRow> pruning;
	private CompositeDnDSupport dndSupport;
	private boolean clearing;

	/**
	 * Initialize filter providers.
	 */
	@Inject
	public Part(@Named(ContextObjects.PATH_PROPERTIES) List<PathPropertyEntry<?>> pathProperties,
			FilterRegistry filters, MApplication app) {
		context = app.getContext();

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
		dndSupport.addListener(e -> fireFilterChanged());
		pruning = new ArrayList<>();
		fireFilterChanged();
	}

	private void addRow() {
		PruningRow newRow = new PruningRow(pruningPanel, providers, true, this::fireFilterChanged);
		pruning.add(newRow);
		newRow.pluginDnD(dndSupport);
		newRow.addDisposeListener(e -> {
			if (!clearing) {
				pruning.remove(newRow);
				fireFilterChanged();
			}
		});
		pruningPanel.getParent().layout();
	}

	private void clearRows() {
		clearing = true;
		pruning.forEach(p -> p.dispose());
		clearing = false;
		pruning.clear();
		pruningPanel.getParent().layout();
		fireFilterChanged();
	}

	private void fireFilterChanged() {
		Collections.sort(pruning, (p1, p2) -> Integer.compare(dndSupport.getOrder(p1), dndSupport.getOrder(p2)));
		Stream<Pruning> pruneStream = pruning.stream().map(p -> p.getPruning()).filter(p -> p != null);

		Pruning base = basePruning.getPruning();
		if (base != null) {
			pruneStream = pruneStream.map(p -> new AndPruning(p, base));
		}

		List<Pruning> result = pruneStream.collect(Collectors.toList());
		if (base != null) {
			result.add(base);
		} else {
			result.add(new PruningWrapper(i -> false));
		}
		context.set(PathFinderProvider.class, new IteratedPriorityDFFinderProvider(result));

		logger.info("Path finder selected: base filter " + basePruning + "; iterations " + formatPruning());
	}

	private String formatPruning() {
		List<PruningRow> rows = pruning.stream().filter(p -> p.getPruning() != null).collect(Collectors.toList());
		if (rows.isEmpty()) {
			return "none";
		} else {
			String result = rows.toString();
			return result.substring(1, result.length() - 1);
		}
	}

	/**
	 * Set path finder provider when focused.
	 */
	@Focus
	public void onFocus() {
		fireFilterChanged();
	}

}

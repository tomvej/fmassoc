package org.tomvej.fmassoc.parts.altsrcdst;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.core.communicate.DataModelTopic;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.altsrcdst.srcdst.SourceDestinationPanel;

/**
 * Part for choosing source and destination tables.
 * 
 * @author Tomáš Vejpustek
 */
public class Part {
	@Inject
	private Logger logger;
	private IEclipseContext context;

	private SourceDestinationPanel srcDst;
	private ForbiddenChooser forbiddenChooser;

	private List<Table> tableSequence;
	private Set<Table> forbidden;

	/**
	 * Initiate visual components.
	 */
	@PostConstruct
	public void createComponents(Composite parent, Shell shell, @Optional DataModel model, MPerspective perspective) {
		context = perspective.getContext();
		parent.setLayout(new GridLayout(2, true));
		GridDataFactory layout = GridDataFactory.fillDefaults().grab(true, true);

		srcDst = new SourceDestinationPanel(parent);
		srcDst.setLayoutData(layout.create());
		srcDst.setTableListener(this::tablesChanged);

		forbiddenChooser = new ForbiddenChooser(parent);
		forbiddenChooser.setLayoutData(layout.create());
		forbiddenChooser.setTableListener(this::forbiddenChanged);

		if (model != null) {
			setTables(model);
		}
	}

	private void tablesChanged(List<Table> tables) {
		tableSequence = tables;
		buildSearchInput();
	}

	private void forbiddenChanged(Set<Table> tables) {
		forbidden = tables;
		buildSearchInput();
	}

	private void buildSearchInput() {
		SearchInput oldInput = context.get(SearchInput.class);
		if (tableSequence != null) {
			SearchInput input = new SearchInput(tableSequence.get(0), tableSequence.subList(1, tableSequence.size()),
					forbidden);
			if (!input.equals(oldInput)) {
				context.set(SearchInput.class, input);
				logger.info("Search input changed to: " + input);
			}
		} else {
			if (oldInput != null) {
				context.remove(SearchInput.class);
				logger.info("Search input cleared.");
			}
		}
	}

	/**
	 * Listens to data model change.
	 */
	@Optional
	@Inject
	public void dataModelChanged(@UIEventTopic(DataModelTopic.MODEL_CHANGED) DataModel model) {
		if (srcDst != null) {
			setTables(model);
		}
	}

	private void setTables(DataModel model) {
		srcDst.setTables(model.getTables());
		forbiddenChooser.setTables(model.getTables(), model.getForbiddenTables());
		forbidden = new HashSet<>(model.getForbiddenTables());
	}

	/**
	 * Puts selected search input to context when it is focused.
	 */
	@Focus
	public void focus() {
		buildSearchInput();
	}
}

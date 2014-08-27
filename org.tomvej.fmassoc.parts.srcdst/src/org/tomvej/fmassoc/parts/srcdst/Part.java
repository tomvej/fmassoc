package org.tomvej.fmassoc.parts.srcdst;

import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.tomvej.fmassoc.core.communicate.DataModelTopic;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.core.widgets.tablechooser.TableChooser;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Part for choosing source and destination tables.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class Part {
	@Inject
	private Logger logger;
	private TableChooser source;
	private DestinationChooser destination;

	private IEclipseContext context;

	/**
	 * Create components comprising this part.
	 */
	@PostConstruct
	public void createComponents(Composite parent, @Optional DataModel model, MPerspective perspective) {
		context = perspective.getContext();

		parent.setLayout(new GridLayout(2, true));
		GridDataFactory layout = GridDataFactory.fillDefaults().grab(true, true);

		Group grp = new Group(parent, SWT.SHADOW_ETCHED_OUT);
		grp.setText("Source table");
		grp.setLayoutData(layout.create());
		grp.setLayout(new GridLayout());
		source = new TableChooser(grp);
		source.setLayoutData(layout.create());

		destination = new DestinationChooser(parent);
		destination.setLayoutData(layout.create());

		source.setTableListener(t -> selectionChanged());
		destination.setTableListener(t -> selectionChanged());

		if (model != null) {
			setTables(model);
		}
	}

	/**
	 * Listen to data model changes.
	 */
	@Inject
	@Optional
	public void dataModelChange(@UIEventTopic(DataModelTopic.MODEL_CHANGED) DataModel model) {
		if (source != null && destination != null) {
			setTables(model);
		}
	}

	private void setTables(DataModel model) {
		Collection<Table> tables = model != null ? model.getTables() : null;
		source.setTables(tables);
		destination.setTables(tables);
	}

	private void selectionChanged() {
		Table source = this.source.getSelection();
		List<Table> destinations = destination.getSelection();
		if (source == null || destinations == null || destinations.isEmpty() || destinations.contains(source)) {
			if (context.get(SearchInput.class) != null) {
				context.set(SearchInput.class, null);
				logger.info("Search input cleared.");
			}
		} else {
			SearchInput input = new SearchInput(source, destinations);
			context.set(SearchInput.class, input);
			logger.info("Search input changed to: " + input);
		}

	}
}

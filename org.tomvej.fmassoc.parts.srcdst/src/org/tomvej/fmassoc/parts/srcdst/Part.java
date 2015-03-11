package org.tomvej.fmassoc.parts.srcdst;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.tomvej.fmassoc.core.communicate.DataModelTopic;
import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.swt.tablechooser.TableChooser;

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
	private ForbiddenChooser forbidden;

	private IEclipseContext context;

	/**
	 * Create components comprising this part.
	 */
	@PostConstruct
	public void createComponents(Composite parent, @Optional DataModel model, MApplication app) {
		context = app.getContext();

		parent.setLayout(new GridLayout(3, true));
		GridDataFactory layout = GridDataFactory.fillDefaults().grab(true, true);

		Group grp = new Group(parent, SWT.SHADOW_ETCHED_OUT);
		grp.setText("Source table");
		grp.setLayoutData(layout.create());
		grp.setLayout(new GridLayout());
		source = new TableChooser(grp);
		source.setLayoutData(layout.create());

		destination = new DestinationChooser(parent);
		destination.setLayoutData(layout.create());

		forbidden = new ForbiddenChooser(parent);
		forbidden.setLayoutData(layout.create());

		source.setTableListener(t -> selectionChanged());
		destination.setTableListener(t -> selectionChanged());
		forbidden.setTableListener(t -> selectionChanged());

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
		forbidden.setTables(tables, model != null ? model.getForbiddenTables() : null);
	}

	private void selectionChanged() {
		Table source = this.source.getSelection();
		List<Table> destinations = destination.getSelection();
		Set<Table> forbidden = this.forbidden.getForbiddenTables();
		SearchInput original = context.get(SearchInput.class);
		if (source == null || destinations == null || destinations.isEmpty() || destinations.contains(source)) {
			if (original != null) {
				context.set(SearchInput.class, null);
				logger.info("Search input cleared.");
			}
		} else {
			SearchInput input = new SearchInput(source, destinations, forbidden);
			if (!input.equals(original)) {
				context.set(SearchInput.class, input);
				logger.info("Search input changed to: " + input);
			}
		}

	}

	/**
	 * Put search input into context on focus.
	 */
	@Focus
	public void focus() {
		selectionChanged();
	}
}

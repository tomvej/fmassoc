package org.tomvej.fmassoc.parts.paths.filter;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;
import org.tomvej.fmassoc.filter.FilterProvider;
import org.tomvej.fmassoc.filter.FilterRegistry;
import org.tomvej.fmassoc.filter.dialog.FilterDialog;

/**
 * Dialog for path table filtering.
 * 
 * @author Tomáš Vejpustek
 */
public class FilterHandler {
	@Inject
	private Logger logger;
	private final FilterDialog dialog;
	private MHandledItem handle;

	/**
	 * Initialize dialog.
	 */
	@Inject
	public FilterHandler(Shell parent) {
		dialog = new FilterDialog(parent);
	}

	/**
	 * Show the dialog and apply chosen filter.
	 */
	@Execute
	public void execute(MHandledItem handle, IEventBroker broker) {
		this.handle = handle;
		boolean prevState = !handle.isSelected();
		handle.setSelected(true);

		dialog.open();
		if (dialog.getReturnCode() == Dialog.OK) {
			broker.post(FilterTopic.FILTER, dialog.getFilter());
			if (dialog.getFilter() != null) {
				handle.setTooltip(dialog.getFilter().toString());
			} else {
				handle.setSelected(false);
				setDefaultTooltip();
			}
		} else {
			handle.setSelected(prevState);
		}
	}

	private void setDefaultTooltip() {
		if (handle != null) {
			handle.setTooltip(handle.getPersistedState().getOrDefault("tooltip", "Filter"));
		}
	}

	private void setSelected(boolean selected) {
		if (handle != null) {
			handle.setSelected(selected);
		}
	}

	/**
	 * Change which properties can be filtered.
	 */
	@Inject
	@Optional
	public void columnsChanged(@UIEventTopic(FilterTopic.COLUMNS) Collection<PathPropertyEntry<?>> properties,
			FilterRegistry filterProviders) {
		Map<PathPropertyEntry<?>, FilterProvider<?>> columns = new HashMap<>();
		for (PathPropertyEntry<?> property : properties) {
			FilterProvider<?> provider = filterProviders.apply(property.getProperty().getType());
			if (provider != null) {
				columns.put(property, provider);
			} else {
				logMissingProvider(property);
			}
		}
		dialog.setColumns(columns);
		setSelected(false);
		setDefaultTooltip();
	}

	private Set<PathPropertyEntry<?>> alreadyLogged = new HashSet<>();

	private void logMissingProvider(PathPropertyEntry<?> property) {
		if (alreadyLogged.contains(property)) {
			return;
		}
		alreadyLogged.add(property);
		logger.warn("No filter provider for " + property.getName() + ".");
	}

}

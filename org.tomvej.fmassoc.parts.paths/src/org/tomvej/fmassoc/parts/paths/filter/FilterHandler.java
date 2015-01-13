package org.tomvej.fmassoc.parts.paths.filter;

import java.util.Collection;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.model.property.PathProperty;

public class FilterHandler {
	private final FilterDialog dialog;
	private MHandledItem handle;


	@Inject
	public FilterHandler(Shell parent) {
		dialog = new FilterDialog(parent);
	}

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

	@Inject
	@Optional
	public void columnsChanged(@UIEventTopic(FilterTopic.COLUMNS) Collection<PathProperty<?>> properties) {
		dialog.setColumns(properties);
		setSelected(false);
		setDefaultTooltip();
	}

}

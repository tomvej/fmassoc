package org.tomvej.fmassoc.parts.paths.multisort;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.tomvej.fmassoc.swt.multisort.MultisortDialog;
import org.tomvej.fmassoc.swt.tables.SortEntry;

/**
 * Dialog for path table multisort.
 * 
 * @author Tomáš Vejpustek
 */
public class MultisortHandler {
	private final MultisortDialog dialog;
	private MHandledItem handle;

	/**
	 * Create dialog.
	 */
	@Inject
	public MultisortHandler(Shell parent, MPart part, EModelService modelService) {
		dialog = new MultisortDialog(parent);

	}

	private void setSelected(boolean selected) {
		if (handle != null) {
			handle.setSelected(selected);
		}
	}

	/**
	 * Show multisort dialog and apply changes (if any).
	 */
	@Execute
	public void execute(IEventBroker broker, MHandledItem handle) {
		// this is not very nice, but I cannot seem to be able to find the item
		// by model service
		this.handle = handle;
		boolean prevState = !handle.isSelected();
		handle.setSelected(true);

		dialog.open();
		if (dialog.getReturnCode() == Dialog.OK) {
			broker.post(MultisortTopic.MULTISORT, dialog.getSort());
			boolean selected = dialog.getSort().size() > 1;
			setSelected(selected);
			if (selected) {
				handle.setTooltip(formatSort(dialog.getSort()));
			} else {
				setDefaultTooltip();
			}
		} else {
			setSelected(prevState);
		}
	}

	private void setDefaultTooltip() {
		if (handle != null) {
			handle.setTooltip(handle.getPersistedState().getOrDefault("tooltip", "Sort wrt Multiple Columns"));
		}
	}

	private String formatSort(List<SortEntry> sort) {
		return sort.stream().map(e -> e.getColumn().getText() + " " + (e.isAscending() ? "/\\" : "V"))
				.collect(Collectors.joining(", "));
	}

	/**
	 * Listen to columns of path table change.
	 */
	@Inject
	@Optional
	public void columnsChanged(@UIEventTopic(MultisortTopic.COLUMNS) Collection<TableColumn> columns) {
		dialog.setColumns(columns);
		setSelected(false);
		setDefaultTooltip();
	}

	/**
	 * Get notified when user uses column sort.
	 */
	@Inject
	@Optional
	public void singleSort(@UIEventTopic(MultisortTopic.SINGLESORT) TableColumn column) {
		// only deselect the handle
		setSelected(false);
		dialog.clearSort();
		setDefaultTooltip();
	}

}
package org.tomvej.fmassoc.parts.paths.filter;

import java.util.Collection;
import java.util.function.Predicate;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.model.property.PathProperty;

/**
 * Dialog for path filtering.
 * 
 * @author Tomáš Vejpustek
 */
public class FilterDialog extends Dialog {

	/**
	 * Initialize dialog.
	 * 
	 * @param parent
	 */
	public FilterDialog(Shell parent) {
		super(parent);
	}

	@Override
	public void create() {
		super.create();
		getShell().setText("Path Filter");
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Set which properties can be filtered.
	 */
	public void setColumns(Collection<PathProperty<?>> columns) {
		// FIXME
	}

	/**
	 * Return actual filter.
	 */
	public Predicate<Path> getFilter() {
		// FIXME
		return null;
	}
}

package org.tomvej.fmassoc.core.widgets.multisort;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;
import org.tomvej.fmassoc.core.tables.SortEntry;

class AscendingEditingSupport extends EditingSupport {
	private final CellEditor editor = new CheckboxCellEditor();

	public AscendingEditingSupport(ColumnViewer viewer) {
		super(viewer);
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected Object getValue(Object element) {
		return ((SortEntry) element).isAscending();
	}

	@Override
	protected void setValue(Object element, Object value) {
		((SimpleSortEntry) element).setAscending((Boolean) value);
		getViewer().update(element, null);
	}


}

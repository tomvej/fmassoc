package org.tomvej.fmassoc.core.tables;

import java.util.List;

import org.eclipse.jface.viewers.ILazyContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * Lazy content provider for a table viewer. Works for a single table viewer.
 * 
 * @author Tomáš Vejpustek
 */
public class ListLazyContentProvider<T> implements ILazyContentProvider {
	private List<T> input;
	private final TableViewer viewer;

	/**
	 * Specify table viewer.
	 */
	public ListLazyContentProvider(TableViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void dispose() {
		// do nothing
	}


	@SuppressWarnings("unchecked")
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		input = (List<T>) newInput;
	};

	@Override
	public void updateElement(int index) {
		viewer.replace(input.get(index), index);
	}
}

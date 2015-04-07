package org.tomvej.fmassoc.swt.wrappers;

import java.util.Arrays;
import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;

/**
 * Wrapper for {@link IListChangeListener} which does the same thing for each
 * {@link ListDiffEntry}.
 * 
 * @author Tomáš Vejpustek
 */
public class ListChangeListenerWrapper implements IListChangeListener {
	private final Consumer<ListDiffEntry> listener;

	/**
	 * Specify action for {@link ListDiffEntry}.
	 */
	public ListChangeListenerWrapper(Consumer<ListDiffEntry> listener) {
		this.listener = Validate.notNull(listener);
	}

	@Override
	public void handleListChange(ListChangeEvent event) {
		Arrays.asList(event.diff.getDifferences()).forEach(listener);
	}
}

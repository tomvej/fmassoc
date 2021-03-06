package org.tomvej.fmassoc.swt.wrappers;

import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * 
 * Wrapper of {@link SelectionListener} so that it can be used functionally.
 * Uses {@link SelectionListener#widgetSelected(SelectionEvent)} method.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class SelectionWrapper extends SelectionAdapter implements SelectionListener {
	private final Consumer<? super SelectionEvent> listener;

	/**
	 * Specify action for
	 * {@link SelectionListener#widgetSelected(SelectionEvent)}.
	 * 
	 * @param listener
	 *            Performed for
	 *            {@link SelectionListener#widgetSelected(SelectionEvent)}
	 */
	public SelectionWrapper(Consumer<? super SelectionEvent> listener) {
		this.listener = Validate.notNull(listener);
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		listener.accept(e);
	}
}

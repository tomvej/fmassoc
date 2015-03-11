package org.tomvej.fmassoc.swt.wrappers;

import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * 
 * Wrapper of {@link SelectionListener} so that it can be used functionally.
 * Uses {@link SelectionListener#widgetDefaultSelected(SelectionEvent)} method.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class DefaultSelectionWrapper extends SelectionAdapter implements SelectionListener {
	private final Consumer<SelectionEvent> listener;

	/**
	 * Specify action for
	 * {@link SelectionListener#widgetDefaultSelected(SelectionEvent)}.
	 * 
	 * @param listener
	 *            Performed for
	 *            {@link SelectionListener#widgetDefaultSelected(SelectionEvent)}
	 */
	public DefaultSelectionWrapper(Consumer<SelectionEvent> listener) {
		this.listener = Validate.notNull(listener);
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		listener.accept(e);
	}
}

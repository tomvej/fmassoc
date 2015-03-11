package org.tomvej.fmassoc.core.wrappers;

import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;

/**
 * Wrapper of {@link FocusListener} so that it can be used functionally.
 * Listens only for {@link FocusListener#focusLost(FocusEvent)}.
 * 
 * @author Tomáš Vejpustek
 */
public class FocusLostWrapper extends FocusAdapter implements FocusListener {
	private final Consumer<FocusEvent> listener;

	/**
	 * Specify action for {@link FocusListener#focusLost(FocusEvent)}.
	 */
	public FocusLostWrapper(Consumer<FocusEvent> listener) {
		this.listener = Validate.notNull(listener);
	}

	@Override
	public void focusLost(FocusEvent e) {
		listener.accept(e);
	}


}

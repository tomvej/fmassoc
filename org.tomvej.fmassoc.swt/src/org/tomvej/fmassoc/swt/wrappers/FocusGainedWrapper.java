package org.tomvej.fmassoc.swt.wrappers;

import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;

/**
 * Wrapper of {@link FocusListener} so that it can be used functionally.
 * Listens only for {@link FocusListener#focusGained(FocusEvent)}.
 * 
 * @author Tomáš Vejpustek
 */
public class FocusGainedWrapper extends FocusAdapter implements FocusListener {
	private final Consumer<FocusEvent> listener;

	/**
	 * Specify action for {@link FocusListener#focusGained(FocusEvent)}.
	 */
	public FocusGainedWrapper(Consumer<FocusEvent> listener) {
		this.listener = Validate.notNull(listener);
	}

	@Override
	public void focusGained(FocusEvent e) {
		listener.accept(e);
	}

}

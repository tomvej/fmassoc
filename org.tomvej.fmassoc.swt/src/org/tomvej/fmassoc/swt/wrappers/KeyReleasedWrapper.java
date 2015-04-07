package org.tomvej.fmassoc.swt.wrappers;

import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

/**
 * Wrapper for {@link KeyListener#keyReleased(KeyEvent)} action. Filters for
 * specified key and mask.
 * 
 * @author Tomáš Vejpustek
 */
public class KeyReleasedWrapper extends KeyAdapter {
	private final int keycode;
	private final int mask;
	private final Consumer<? super KeyEvent> listener;

	/**
	 * Specify key code, mask and action for
	 * {@link KeyListener#keyReleased(KeyEvent)}.
	 */
	public KeyReleasedWrapper(int keyCode, int mask, Consumer<? super KeyEvent> listener) {
		this.keycode = keyCode;
		this.mask = mask;
		this.listener = Validate.notNull(listener);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.keyCode == keycode && e.stateMask == mask) {
			listener.accept(e);
		}
	}

}

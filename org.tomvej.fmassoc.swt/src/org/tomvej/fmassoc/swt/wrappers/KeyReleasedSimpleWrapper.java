package org.tomvej.fmassoc.swt.wrappers;

import java.util.function.Consumer;

import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;

/**
 * Wrapper for {@link KeyListener#keyReleased(KeyEvent)}.
 * 
 * @author Tomáš Vejpustek
 */
public class KeyReleasedSimpleWrapper extends KeyAdapter implements KeyListener {
	private final Consumer<KeyEvent> listener;

	/**
	 * Specify action for {@link KeyListener#keyReleased(KeyEvent)}.
	 */
	public KeyReleasedSimpleWrapper(Consumer<KeyEvent> listener) {
		this.listener = listener;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		listener.accept(e);
	}

}

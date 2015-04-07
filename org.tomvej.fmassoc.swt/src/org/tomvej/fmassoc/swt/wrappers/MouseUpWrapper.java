package org.tomvej.fmassoc.swt.wrappers;

import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;

/**
 * Wrapper of {@link MouseListener} so that it can be used functionally.
 * Listens for {@link MouseListener#mouseUp(MouseEvent)} action.
 * 
 * @author Tomáš Vejpustek
 */
public class MouseUpWrapper implements MouseListener {
	private final Consumer<? super MouseEvent> listener;

	/**
	 * Specify action for {@link MouseListener#mouseUp(MouseEvent)}.
	 */
	public MouseUpWrapper(Consumer<? super MouseEvent> listener) {
		this.listener = Validate.notNull(listener);
	}

	@Override
	public void mouseUp(MouseEvent e) {
		listener.accept(e);
	};

	@Override
	public void mouseDown(MouseEvent e) {
		// do nothing
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// do nothing
	}

}

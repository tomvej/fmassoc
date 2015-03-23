package org.tomvej.fmassoc.swt.wrappers;

import java.util.function.Consumer;

import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;

/**
 * Simulates mouse click behaviour. Use only if you cannot use other listener
 * (such as {@link FocusListener} or {@link SelectionListener}).
 * 
 * @author Tomáš Vejpustek
 */
// uses http://www.codeaffine.com/2014/11/26/swt-mouse-click-implementation/
public class MouseClickWrapper implements MouseListener {
	private final Consumer<? super MouseEvent> listener;
	private boolean armed;

	/**
	 * Specify action for {@link MouseListener#mouseUp(MouseEvent)} when it is
	 * part of a mouse click.
	 */
	public MouseClickWrapper(Consumer<? super MouseEvent> listener) {
		this.listener = listener;
	}

	@Override
	public void mouseDown(MouseEvent e) {
		if (e.button == 1) {
			armed = true;
		}
	}

	@Override
	public void mouseUp(MouseEvent e) {
		if (armed && inRange(e)) {
			listener.accept(e);
		}
		armed = false;
	}

	private boolean inRange(MouseEvent event) {
		Point size = ((Control) event.getSource()).getSize();
		return event.x >= 0 && event.y >= 0 &&
				event.x <= size.x && event.y <= size.y;
	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		// do nothing
	}

}

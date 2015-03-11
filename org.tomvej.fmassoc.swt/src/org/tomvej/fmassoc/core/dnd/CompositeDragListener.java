package org.tomvej.fmassoc.core.dnd;

import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

/**
 * Mostly handles only drag image and visibility.
 * 
 * @author Tomáš Vejpustek
 */
class CompositeDragListener extends DragSourceAdapter {
	private final Control parent;
	private Image image;

	public CompositeDragListener(Control parent) {
		this.parent = parent;
	}

	@Override
	public void dragStart(DragSourceEvent event) {
		Point size = parent.getSize();

		GC gc = new GC(parent);
		image = new Image(Display.getCurrent(), size.x, size.y);
		gc.copyArea(image, 0, 0);
		event.image = image;
		gc.dispose();

		parent.setVisible(false);
	}

	@Override
	public void dragFinished(DragSourceEvent event) {
		image.dispose();
		parent.setVisible(true);
	}

}

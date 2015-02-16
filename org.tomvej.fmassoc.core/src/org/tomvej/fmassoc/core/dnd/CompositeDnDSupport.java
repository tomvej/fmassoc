package org.tomvej.fmassoc.core.dnd;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Support drag and drop for components of a parent composite.
 * 
 * @author Tomáš Vejpustek
 */
public class CompositeDnDSupport {
	private static final Transfer[] TRANSFERS = { LocalSelectionTransfer.getTransfer() };

	private final Composite parent;
	private final Map<Control, Integer> order = new HashMap<>();

	private Control selected;

	/**
	 * Specify parent composite.
	 */
	public CompositeDnDSupport(Composite parent) {
		this.parent = parent;
		DropTarget trg = new DropTarget(parent, DND.DROP_MOVE);
		trg.setTransfer(TRANSFERS);
	}

	/**
	 * Register draggable child component.
	 * 
	 * @param knob
	 *            Source of drag events -- part of child component which can be
	 *            dragged.
	 * @param component
	 *            Child control which can be dragged.
	 */
	public void registerKnob(Control knob, Control component) {
		if (!component.getParent().equals(parent)) {
			throw new IllegalArgumentException("Control must be a child of DnD parent.");
		}

		DragSource src = new DragSource(knob, DND.DROP_MOVE);
		src.setTransfer(TRANSFERS);
		src.addDragListener(new CompositeDragListener(component));
		src.addDragListener(new DragSourceAdapter() {
			@Override
			public void dragStart(DragSourceEvent event) {
				selected = component;
				regenerateOrder();
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				selected = null;
			}
		});

		DropTarget trg = new DropTarget(component, DND.DROP_MOVE);
		trg.setTransfer(TRANSFERS);
		trg.addDropListener(new DropListener(component));

	}

	/**
	 * Return order of child component.
	 */
	public int getOrder(Control child) {
		return order.get(child);
	}

	private void regenerateOrder() {
		Control[] children = parent.getChildren();
		for (int i = 0; i < children.length; i++) {
			order.put(children[i], i);
		}
	}

	private class DropListener extends DropTargetAdapter {
		private final Control panel;

		public DropListener(Control panel) {
			this.panel = panel;
		}

		@Override
		public void dragOver(DropTargetEvent event) {
			if (getOrder(selected) > getOrder(panel)) {
				selected.moveAbove(panel);
			} else {
				selected.moveBelow(panel);
			}

			// reorder fields
			parent.layout();

			// redraw component
			Point size = panel.getSize();
			panel.redraw(0, 0, size.x, size.y, true);

			regenerateOrder();
		}
	}

}

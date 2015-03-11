package org.tomvej.fmassoc.swt.dnd;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * Composite has been dragged to a new position.
 * 
 * @author Tomáš Vejpustek
 */
public class CompositeDnDEvent {
	private final CompositeDnDSupport source;
	private final Composite parent;
	private final Control component;
	private final int oldOrder, newOrder;

	/**
	 * Specify circumstances.
	 * 
	 * @param source
	 * @param parent
	 * @param component
	 * @param oldOrder
	 * @param newOrder
	 */
	public CompositeDnDEvent(CompositeDnDSupport source, Composite parent, Control component, int oldOrder, int newOrder) {
		this.source = source;
		this.parent = parent;
		this.component = component;
		this.oldOrder = oldOrder;
		this.newOrder = newOrder;
	}

	/**
	 * Return DnD support generating this event.
	 */
	public CompositeDnDSupport getSource() {
		return source;
	}

	/**
	 * Return parent composite of dragged component.
	 */
	public Composite getParent() {
		return parent;
	}

	/**
	 * Return dragged component.
	 */
	public Control getComponent() {
		return component;
	}

	/**
	 * Old order of the dragged component.
	 */
	public int getOldOrder() {
		return oldOrder;
	}

	/**
	 * New order of the dragged component.
	 */
	public int getNewOrder() {
		return newOrder;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [" + source + " : " + oldOrder + " -> " + newOrder + "]";
	}

}

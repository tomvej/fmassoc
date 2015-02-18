package org.tomvej.fmassoc.core.dnd;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class CompositeDnDEvent {
	private final CompositeDnDSupport source;
	private final Composite parent;
	private final Control component;
	private final int oldOrder, newOrder;

	public CompositeDnDEvent(CompositeDnDSupport source, Composite parent, Control component, int oldOrder, int newOrder) {
		this.source = source;
		this.parent = parent;
		this.component = component;
		this.oldOrder = oldOrder;
		this.newOrder = newOrder;
	}

	public CompositeDnDSupport getSource() {
		return source;
	}

	public Composite getParent() {
		return parent;
	}

	public Control getComponent() {
		return component;
	}

	public int getOldOrder() {
		return oldOrder;
	}

	public int getNewOrder() {
		return newOrder;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + " [" + source + " : " + oldOrder + " -> " + newOrder + "]";
	}

}

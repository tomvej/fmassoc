package org.tomvej.fmassoc.swt.wrappers;

import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;

/**
 * Wrapper for {@link TraverseListener#keyTraversed(TraverseEvent)}. Filters
 * action detail.
 * 
 * @author Tomáš Vejpustek
 */
public class TraverseWrapper implements TraverseListener {
	private final Consumer<? super TraverseEvent> listener;
	private final int detail;


	/**
	 * Specify action for {@link TraverseListener#keyTraversed(TraverseEvent)}
	 * and traverse detail.
	 */
	public TraverseWrapper(int detail, Consumer<? super TraverseEvent> listener) {
		this.listener = Validate.notNull(listener);
		this.detail = detail;
	}


	@Override
	public void keyTraversed(TraverseEvent e) {
		if (e.detail == detail) {
			listener.accept(e);
		}
	}
}

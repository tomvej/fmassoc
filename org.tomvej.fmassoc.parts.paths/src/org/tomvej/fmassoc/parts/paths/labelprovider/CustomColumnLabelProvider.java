package org.tomvej.fmassoc.parts.paths.labelprovider;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

/**
 * Wrapper for label provider. Use in a similar way to
 * {@link ColumnLabelProvider}.
 * 
 * @author Tomáš Vejpustek
 * @param <T>
 */
public interface CustomColumnLabelProvider<T> {
	/**
	 * @see ColumnLabelProvider#getBackground(Object)
	 */
	default Color getBackground(T element) {
		return null;
	}

	/**
	 * @see ColumnLabelProvider#getForeground(Object)
	 */
	default Color getForeground(T element) {
		return null;
	}

	/**
	 * @see ColumnLabelProvider#dispose()
	 */
	default void dispose() {
		// do nothing
	}

	/**
	 * @see ColumnLabelProvider#getImage(Object)
	 */
	default Image getImage(T element) {
		return null;
	}

	/**
	 * @see ColumnLabelProvider#getText(Object)
	 */
	String getText(T element);

	/**
	 * @see ColumnLabelProvider#getToolTipBackgroundColor(Object)
	 */
	default Color getToolTipBackgroundColor(T element) {
		return null;
	}

	/**
	 * @see ColumnLabelProvider#getToolTipForegroundColor(Object)
	 */
	default Color getToolTipForegroundColor(T element) {
		return null;
	}

	/**
	 * @see ColumnLabelProvider#getToolTipImage(Object)
	 */
	default Image getToolTipImage(T element) {
		return null;
	}

	/**
	 * @see ColumnLabelProvider#getToolTipText(Object)
	 */
	default String getToolTipText(T element) {
		return null;
	}
}

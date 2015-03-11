package org.tomvej.fmassoc.swt.wrappers;

import java.util.function.Function;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * Allows {@link ColumnLabelProvider} to be used functionally. Provides only
 * text label.
 * 
 * Warning: Potentially type-unsafe. Use only when you are sure the type of
 * values {@code <T>}.
 * 
 * @param <T>
 *            Type of row values.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public class TextColumnLabelProvider<T> extends ColumnLabelProvider {
	private final Function<T, String> provider;

	/**
	 * Specify way of obtaining label from row values.
	 */
	public TextColumnLabelProvider(Function<T, String> labelProvider) {
		provider = Validate.notNull(labelProvider);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getText(Object element) {
		T target = (T) element;
		return provider.apply(target);
	}

}
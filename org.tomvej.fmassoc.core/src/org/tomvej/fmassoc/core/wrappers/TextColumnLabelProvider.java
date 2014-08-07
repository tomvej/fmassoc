package org.tomvej.fmassoc.core.wrappers;

import java.util.function.Function;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * Allows {@link ColumnLabelProvider} to be used functionally.
 * 
 * Warning: Potentially type-unsafe. Use only when you are sure the type of
 * values {@code <T>}.
 * 
 * @param <T>
 *            Type of values.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public class TextColumnLabelProvider<T> extends ColumnLabelProvider {
	private final Function<T, String> provider;

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
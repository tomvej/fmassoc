package org.tomvej.fmassoc.core.wrappers;

import java.util.function.Function;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * Allows {@link LabelProvider} to be used functionally. Provides only text
 * label.
 * 
 * Warning: Potentially type-unsafe. Use only when you are sure the type of
 * values {@code <T>}.
 * 
 * @author Tomáš Vejpustek
 *
 * @param <T>
 *            Type of row values.
 */
public class TextLabelProvider<T> extends LabelProvider {
	private final Function<T, String> provider;

	/**
	 * Specify way of obtaining label from row values.
	 */
	public TextLabelProvider(Function<T, String> provider) {
		this.provider = Validate.notNull(provider);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String getText(Object element) {
		return provider.apply((T) element);
	}
}

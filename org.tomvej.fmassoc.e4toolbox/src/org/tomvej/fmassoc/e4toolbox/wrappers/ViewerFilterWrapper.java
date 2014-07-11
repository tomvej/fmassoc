package org.tomvej.fmassoc.e4toolbox.wrappers;

import java.util.function.Predicate;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * 
 * <p>
 * Wrapper of {@link ViewerFilter} so that it can be used functionally. Example
 * usage:
 * </p>
 * 
 * <code>
 *   viewer.addFilter(new ViewerFilterWrapper&lt;AssociationInfo&gt;(assoc -&gt; assoc.isMandatory()));
 *  </pre>
 * 
 * <p>
 * <b>Warning:</b> Contains potentially unsafe type-casting. Make sure the
 * correct type parameter.
 * </p>
 * 
 * 
 * 
 * @author Tomáš Vejpustek
 *
 * @param <E>
 *            Type of elements contained by parent table.
 */
public class ViewerFilterWrapper<E> extends ViewerFilter {
	private final Predicate<E> filter;

	/**
	 * Specify predicate which allows elements to be displayed.
	 * 
	 * @param shouldShow
	 *            returns {@code true} when element should be displayed,
	 *            {@code false} otherwise.
	 */
	public ViewerFilterWrapper(Predicate<E> shouldShow) {
		filter = shouldShow;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		return filter.test((E) element);
	}
}
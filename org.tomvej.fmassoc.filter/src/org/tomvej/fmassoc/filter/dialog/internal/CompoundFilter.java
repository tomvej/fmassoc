package org.tomvej.fmassoc.filter.dialog.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;

/**
 * Path filter made as a conjunction of another filters. Mostly a wrapper for
 * {@link #toString()} function.
 * 
 * @author Tomáš Vejpustek
 */
public class CompoundFilter<T> implements Predicate<T> {
	private final Collection<Predicate<T>> filters;

	/**
	 * Specify component filters.
	 */
	public CompoundFilter(Collection<? extends Predicate<T>> filters) {
		this.filters = Collections.unmodifiableCollection(Validate.noNullElements(filters));
	}

	@Override
	public boolean test(T t) {
		return filters.stream().allMatch(f -> f.test(t));
	}

	@Override
	public String toString() {
		return filters.stream().map(Object::toString).collect(Collectors.joining(", "));
	}

}

package org.tomvej.fmassoc.filter.dialog.internal;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Predicate;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Path filter made as a conjunction of another filters. Mostly a wrapper for
 * {@link #toString()} function.
 * 
 * @author Tomáš Vejpustek
 */
public class CompoundFilter implements Predicate<Path> {
	private final Collection<Predicate<Path>> filters;

	/**
	 * Specify component filters.
	 */
	public CompoundFilter(Collection<? extends Predicate<Path>> filters) {
		this.filters = Collections.unmodifiableCollection(Validate.noNullElements(filters));
	}

	@Override
	public boolean test(Path t) {
		return filters.stream().allMatch(f -> f.test(t));
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		filters.forEach(e -> result.append(e).append(", "));
		int length = result.length();
		if (length > 0) {
			result.delete(length - 2, length);
		}
		return result.toString();
	}

}

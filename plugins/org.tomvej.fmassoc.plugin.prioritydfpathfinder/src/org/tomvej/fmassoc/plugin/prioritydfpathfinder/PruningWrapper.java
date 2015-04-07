package org.tomvej.fmassoc.plugin.prioritydfpathfinder;

import java.util.Collection;
import java.util.function.Predicate;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.path.PathInfo;
import org.tomvej.fmassoc.model.property.PathProperty;

/**
 * Wrapper for functional use of pruning. Stores set of propeties. The path is
 * pruned when supplied predicate return {@code true}.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class PruningWrapper extends AbstractPruning {
	private final Predicate<PathInfo> test;

	/**
	 * Specify predicate and properties.
	 */
	public PruningWrapper(Predicate<PathInfo> test, PathProperty<?> property) {
		super(property);
		this.test = Validate.notNull(test);
	}

	/**
	 * Specify predicate and properties.
	 */
	public PruningWrapper(Predicate<PathInfo> test, PathProperty<?>... properties) {
		super(properties);
		this.test = Validate.notNull(test);
	}

	/**
	 * Specify predicate and properties.
	 */
	public PruningWrapper(Predicate<PathInfo> test, Collection<PathProperty<?>> properties) {
		super(properties);
		this.test = Validate.notNull(test);
	}

	@Override
	public boolean prune(PathInfo target) {
		return test.test(target);
	}

}

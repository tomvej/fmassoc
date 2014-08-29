package org.tomvej.fmassoc.plugin.prioritydfpathfinder;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.property.PathProperty;

/**
 * Base class for prunings which stores used properties.
 * 
 * @author Tomáš Vejpustek
 *
 */
public abstract class AbstractPruning implements Pruning {
	private final Set<PathProperty<?>> properties;

	/**
	 * Specify used properties.
	 */
	public AbstractPruning(PathProperty<?> property) {
		this.properties = Collections.singleton(Validate.notNull(property));
	}

	/**
	 * Specify used properties.
	 */
	public AbstractPruning(Collection<PathProperty<?>> properties) {
		this.properties = Collections.unmodifiableSet(new HashSet<>(properties));
		Validate.isTrue(!this.properties.contains(null), "There is null element inside supplied properties.");
	}

	/**
	 * Specify used properties.
	 */
	public AbstractPruning(PathProperty<?>... properties) {
		this(Arrays.asList(properties));
	}

	@Override
	public Set<PathProperty<?>> getUsedProperties() {
		return properties;
	}

}

package org.tomvej.fmassoc.finder.priority;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
	public AbstractPruning(PathProperty<?>... properties) {
		this.properties = new HashSet<>(Arrays.asList(properties));
	}

	@Override
	public Set<PathProperty<?>> getUsedProperties() {
		return properties;
	}

}

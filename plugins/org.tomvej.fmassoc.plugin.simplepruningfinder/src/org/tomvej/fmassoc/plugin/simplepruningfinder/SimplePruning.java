package org.tomvej.fmassoc.plugin.simplepruningfinder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.path.PathInfo;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.plugin.prioritydfpathfinder.Pruning;

public class SimplePruning implements Pruning {
	private final Settings settings;
	private final Set<PathProperty<?>> properties;


	public SimplePruning(Settings settings) {
		this.settings = Validate.notNull(settings);

		properties = new HashSet<>();
		// FIXME add properties
	}

	@Override
	public Set<PathProperty<?>> getUsedProperties() {
		return Collections.unmodifiableSet(properties);
	}

	@Override
	public boolean prune(PathInfo target) {
		// TODO Auto-generated method stub
		return false;
	}

}

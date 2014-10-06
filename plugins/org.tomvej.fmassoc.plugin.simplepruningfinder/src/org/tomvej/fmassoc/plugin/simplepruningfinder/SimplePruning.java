package org.tomvej.fmassoc.plugin.simplepruningfinder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.Multiplicity;
import org.tomvej.fmassoc.model.path.PathInfo;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.plugin.prioritydfpathfinder.Pruning;
import org.tomvej.fmassoc.plugin.properties.basic.PathLength;
import org.tomvej.fmassoc.plugin.properties.basic.PathMultiplicity;
import org.tomvej.fmassoc.plugin.properties.basic.PathOptionality;
import org.tomvej.fmassoc.plugin.properties.basic.PathWidth;

/**
 * Pruning constructed from settings.
 * 
 * @author Tomáš Vejpustek
 */
public class SimplePruning implements Pruning {
	private static final PathOptionality OPTIONAL = new PathOptionality();
	private static final PathMultiplicity MULT = new PathMultiplicity();
	private static final PathLength LENGTH = new PathLength();
	private static final PathWidth WIDTH = new PathWidth();

	private final Settings settings;
	private final Set<PathProperty<?>> properties;

	/**
	 * Specify settings.
	 */
	public SimplePruning(Settings settings) {
		this.settings = Validate.notNull(settings);

		properties = new HashSet<>();
		properties.add(LENGTH);
		properties.add(WIDTH);
		if (!settings.searchOptional()) {
			properties.add(OPTIONAL);
		}
		if (!settings.searchMN()) {
			properties.add(MULT);
		}
	}

	@Override
	public Set<PathProperty<?>> getUsedProperties() {
		return Collections.unmodifiableSet(properties);
	}

	@Override
	public boolean prune(PathInfo target) {
		if (target.getProperty(WIDTH) > settings.getWidthLimit()) {
			return true;
		}
		if (target.getProperty(LENGTH) > settings.getLengthLimit()) {
			return true;
		}
		if (!settings.searchOptional() && !target.getProperty(OPTIONAL)) {
			return true;
		}
		if (!settings.searchMN() && target.getProperty(MULT).equals(Multiplicity.MANY_TO_MANY)) {
			return true;
		}
		return false;
	}

}

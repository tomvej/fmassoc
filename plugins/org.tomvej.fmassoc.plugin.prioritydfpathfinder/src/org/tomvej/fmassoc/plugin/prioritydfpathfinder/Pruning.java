package org.tomvej.fmassoc.plugin.prioritydfpathfinder;

import java.util.Set;

import org.tomvej.fmassoc.model.path.PathInfo;
import org.tomvej.fmassoc.model.property.PathProperty;

/**
 * 
 * Filter which prunes paths.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public interface Pruning {

	/**
	 * Returns list of properties used in {@link #prune(PathInfo)}. The
	 * programmer
	 * <b>must</b> ensure all properties are defined here -- otherwise,
	 * unexpected errors will occur.
	 */
	Set<PathProperty<?>> getUsedProperties();

	/**
	 * Checks whether given path should be pruned.
	 * 
	 * @return {@code true} when this path should be pruned and not extended
	 *         anymore, {@code false} otherwise.
	 */
	boolean prune(PathInfo target);
}

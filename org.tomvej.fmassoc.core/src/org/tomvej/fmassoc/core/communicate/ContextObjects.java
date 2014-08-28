package org.tomvej.fmassoc.core.communicate;

import java.util.List;

import org.tomvej.fmassoc.model.path.Path;

/**
 * List of constants for named context objects provided by this plugin.
 * 
 * @author vejpustekt
 *
 */
public interface ContextObjects {
	/** Id of this plugin. */
	public static final String PLUGIN = "org.tomvej.fmassoc.core";
	/**
	 * List of found paths. Unmodifiable {@link List} of {@link Path} (
	 * {@code List<Path>}).
	 */
	public static final String FOUND_PATHS = PLUGIN + ".foundPaths";
}

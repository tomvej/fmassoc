package org.tomvej.fmassoc.core.communicate;

import java.util.List;

import org.tomvej.fmassoc.core.properties.PathPropertyEntry;
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
	/**
	 * List of path properties. Unmodifiable {@link List} of
	 * {@link PathPropertyEntry} ({@code List<PathPropertyEntry>}).
	 */
	public static final String PATH_PROPERTIES = PLUGIN + ".pathProperties";
	/**
	 * Currently selected path transformed via the pinned transformer.
	 * {@link String}.
	 */
	public static final String TRANSFORMED_PATH = PLUGIN + ".transformedPath";
	/**
	 * Currently pinned transformation part.
	 */
	public static final String TRANSFORMATION_PART = PLUGIN + ".transformationPart";
}

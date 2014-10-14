package org.tomvej.fmassoc.core.search.internal;

/**
 * Constants for accessing path search preferences.
 * 
 * @author Tomáš Vejpustek
 */
public interface PathSearchPreference {
	/** Preference node name */
	public static final String NODE = "org.tomvej.fmassoc.core.search";
	/** Maximum found paths. Integer (since list size is). */
	public static final String PATH_LIMIT = "path.limit";
	/**
	 * Whether "Too many path found, search stopped." message dialog should be
	 * shown. Boolean.
	 */
	public static final String SHOW_PATH_LIMIT_REACHED = "path.limit.show";
}

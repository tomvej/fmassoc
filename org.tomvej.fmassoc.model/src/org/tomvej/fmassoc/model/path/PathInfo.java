package org.tomvej.fmassoc.model.path;

import org.tomvej.fmassoc.model.property.PathProperty;


/**
 * Information about path.
 * 
 * @author Tomáš Vejpustek
 *
 */
public interface PathInfo {
	/**
	 * Return path length.
	 */
	int getLength();

	/**
	 * Return specified property value for given path.
	 * 
	 * @return Property value when the path carries the specified property,
	 *         {@code null} otherwise.
	 */
	<T> T getProperty(PathProperty<T> property);

}

package org.tomvej.fmassoc.model.property;

import org.tomvej.fmassoc.model.db.AssociationInfo;

/**
 * Algorithm which computes property value for a path by building the path
 * and updating property value when each association is added/removed.
 * 
 * @author Tomáš Vejpustek
 *
 * @param <T>
 *            Type of property values.
 */
public interface PathPropertyBuilder<T> {
	/**
	 * Get value of current path.
	 */
	T getValue();

	/**
	 * Add an association to the path.
	 */
	void push(AssociationInfo association);

	/**
	 * Remove the last association from the path.
	 */
	void pop(AssociationInfo association);
}

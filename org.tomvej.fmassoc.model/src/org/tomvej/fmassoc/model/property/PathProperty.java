package org.tomvej.fmassoc.model.property;

import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.path.Path;


/**
 * Property pertaining to a path (e.g. length, M:N width, ...). Essentially a
 * catamorphism on paths.
 * 
 * @author Tomáš Vejpustek
 *
 * @param <T>
 *            Type of property value.
 */
public interface PathProperty<T> {
	/**
	 * Returns property value for given path.
	 */
	default T getValue(Path target) {
		T result = target.getProperty(this);
		if (result == null) {
			PathPropertyBuilder<T> builder = getBuilder();
			for (AssociationProperty association : target.getAssociations()) {
				builder.push(association);
			}
			result = builder.getValue();
		}
		return result;
	}

	/**
	 * Returns type of this property values.
	 */
	Class<T> getType();

	/**
	 * Returns an algorithm which may be used to compute property value for
	 * given path.
	 */
	PathPropertyBuilder<T> getBuilder();
}

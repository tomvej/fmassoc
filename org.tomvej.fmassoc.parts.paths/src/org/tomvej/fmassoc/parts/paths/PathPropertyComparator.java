package org.tomvej.fmassoc.parts.paths;

import java.util.Comparator;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.model.property.PathProperty;

/**
 * Comparator for path properties. First compares by given comparator, then by
 * property values -- if they are comparable -- and lastly by their string
 * representation.
 * 
 * @author Tomáš Vejpustek
 */
public class PathPropertyComparator<T> implements Comparator<Path> {
	private final PathProperty<T> property;
	private final Comparator<T> comparator;


	/**
	 * Specify property and comparator.
	 */
	public PathPropertyComparator(PathProperty<T> property, Comparator<T> comparator) {
		this.property = Validate.notNull(property);
		this.comparator = comparator;
	}

	/**
	 * Specify only property.
	 */
	public PathPropertyComparator(PathProperty<T> property) {
		this(property, null);
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public int compare(Path o1, Path o2) {
		T v1 = property.getValue(o1);
		T v2 = property.getValue(o2);
		if (comparator != null) {
			return comparator.compare(v1, v2);
		}
		if (v1 instanceof Comparable) {
			return ((Comparable) v1).compareTo(v2);
		}
		return v1.toString().compareTo(v2.toString());
	}

}

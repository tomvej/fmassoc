package org.tomvej.fmassoc.test.path.aggregator;

import java.util.Comparator;

/**
 * Comparator for comparable types.
 * 
 * @author Tomáš Vejpustek
 */
public class ComparableComparator<T extends Comparable<? super T>> implements Comparator<T> {

	@Override
	public int compare(T o1, T o2) {
		return o1.compareTo(o2);
	}

}

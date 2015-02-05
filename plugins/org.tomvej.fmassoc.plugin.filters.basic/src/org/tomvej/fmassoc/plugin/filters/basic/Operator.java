package org.tomvej.fmassoc.plugin.filters.basic;

/**
 * Binary predicate.
 * 
 * @author Tomáš Vejpustek
 * @see OperatorFilter
 * @param <T>
 *            type of predicate values
 */
@FunctionalInterface
public interface Operator<T> {

	/**
	 * Test whether values satisfy the predicate.
	 */
	boolean test(T t1, T t2);
}

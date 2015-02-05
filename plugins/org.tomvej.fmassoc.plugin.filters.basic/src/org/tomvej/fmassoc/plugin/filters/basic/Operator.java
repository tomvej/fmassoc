package org.tomvej.fmassoc.plugin.filters.basic;

@FunctionalInterface
public interface Operator<T> {

	boolean test(T t1, T t2);
}

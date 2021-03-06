package org.tomvej.fmassoc.plugin.filters.basic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.tomvej.fmassoc.filter.Filter;
import org.tomvej.fmassoc.filter.FilterProvider;
import org.tomvej.fmassoc.plugins.filters.basic.operator.ComparableOperator;
import org.tomvej.fmassoc.plugins.filters.basic.operator.ObjectOperator;
import org.tomvej.fmassoc.plugins.filters.basic.operator.Operator;
import org.tomvej.fmassoc.plugins.filters.basic.operator.OperatorFilter;
import org.tomvej.fmassoc.plugins.filters.basic.operator.SpinnerProvider;

/**
 * Provider of integer filter.
 * 
 * @author Tomáš Vejpustek
 * @see OperatorFilter
 */
public class IntegerFilterProvider implements FilterProvider<Integer> {

	@Override
	public Filter<Integer> get() {
		Collection<Operator<? super Integer>> operators = new ArrayList<>();
		operators.addAll(Arrays.asList(ObjectOperator.values()));
		operators.addAll(Arrays.asList(ComparableOperator.values()));
		return new OperatorFilter<>(operators, new SpinnerProvider());
	}
}

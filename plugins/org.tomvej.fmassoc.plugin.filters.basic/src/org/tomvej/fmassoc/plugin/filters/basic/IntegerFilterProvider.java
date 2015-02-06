package org.tomvej.fmassoc.plugin.filters.basic;

import java.util.Collections;
import java.util.Objects;

import org.tomvej.fmassoc.parts.paths.filterprovider.Filter;
import org.tomvej.fmassoc.parts.paths.filterprovider.FilterProvider;

/**
 * Provider of integer filter.
 * 
 * @author Tomáš Vejpustek
 * @see OperatorFilter
 */
public class IntegerFilterProvider implements FilterProvider<Integer> {

	@Override
	public Filter<Integer> get() {
		return new OperatorFilter<>(Collections.singletonList(new Operator<Integer>() {
			@Override
			public boolean test(Integer t1, Integer t2) {
				return Objects.equals(t1, t2);
			}

			@Override
			public String toString() {
				return "=";
			}
		}), new SpinnerProvider());
	}
}

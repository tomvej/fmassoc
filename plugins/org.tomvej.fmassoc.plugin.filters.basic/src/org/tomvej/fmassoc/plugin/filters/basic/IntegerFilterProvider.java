package org.tomvej.fmassoc.plugin.filters.basic;

import java.util.Collections;
import java.util.Objects;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
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
		}), new ValueControlProvider<Integer>() {
			@Override
			public Control createControl(Composite parent, Consumer<Integer> listener, Integer initial) {
				Spinner input = new Spinner(parent, SWT.BORDER);
				if (initial == null) {
					initial = 0;
				}
				input.setValues(initial, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1, 10);
				input.addModifyListener(e -> listener.accept(input.getSelection()));
				return input;
			}
		});
	}
}

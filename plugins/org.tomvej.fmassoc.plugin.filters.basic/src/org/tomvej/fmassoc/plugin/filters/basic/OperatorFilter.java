package org.tomvej.fmassoc.plugin.filters.basic;

import java.util.Collection;
import java.util.function.Predicate;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.tomvej.fmassoc.parts.paths.filterprovider.Filter;

/**
 * Filter which works on basis of binary predicates where the predicate (or
 * operator) and one value are specified via the visual component. The other
 * value is the filtered value.
 * 
 * @author Tomáš Vejpustek
 * @param <T>
 *            type of filtered value
 */
public class OperatorFilter<T> implements Filter<T> {
	private final Collection<Operator<? super T>> operators;
	private final ValueControlProvider<T> controlProvider;

	private Operator<? super T> operator;
	private T value;

	/**
	 * Specify predicates (operators) and provider of visual component for
	 * specifying one of the values.
	 */
	public OperatorFilter(Collection<Operator<? super T>> operators, ValueControlProvider<T> controlProvider) {
		this.operators = Validate.notEmpty(Validate.noNullElements(operators));
		this.controlProvider = Validate.notNull(controlProvider);
		operator = operators.iterator().next();
	}

	@Override
	public Control createFilterPanel(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));

		ComboViewer combo = new ComboViewer(container, SWT.READ_ONLY);
		combo.setContentProvider(ArrayContentProvider.getInstance());
		combo.setInput(operators);

		combo.setSelection(new StructuredSelection(operator));
		combo.addSelectionChangedListener(
				e -> operator = getSelection(combo.getSelection()));

		controlProvider.createControl(container, v -> value = v, value);

		return container;
	}

	@SuppressWarnings("unchecked")
	private Operator<? super T> getSelection(ISelection selection) {
		return (Operator<? super T>) ((IStructuredSelection) selection).getFirstElement();
	}

	@Override
	public Predicate<T> getFilter() {
		return new Predicate<T>() {
			@Override
			public boolean test(T t) {
				return operator.test(t, value);
			}

			@Override
			public String toString() {
				return operator + " " + value;
			}
		};
	}


}

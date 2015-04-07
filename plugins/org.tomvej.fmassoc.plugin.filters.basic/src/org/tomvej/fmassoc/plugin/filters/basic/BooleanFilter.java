package org.tomvej.fmassoc.plugin.filters.basic;

import java.util.function.Predicate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.tomvej.fmassoc.filter.Filter;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;

/**
 * Filter for boolean values -- simple check box.
 * 
 * @author Tomáš Vejpustek
 */
public class BooleanFilter implements Filter<Boolean> {
	private boolean selected = false;

	@Override
	public Predicate<Boolean> getFilter() {
		return new Predicate<Boolean>() {

			@Override
			public boolean test(Boolean t) {
				return selected ? t : !t;
			}

			@Override
			public String toString() {
				return selected ? ": yes" : ": no";
			}
		};
	}

	@Override
	public Control createFilterPanel(Composite parent) {
		Button result = new Button(parent, SWT.CHECK);
		result.setSelection(selected);
		result.addSelectionListener(new SelectionWrapper(e -> selected = result.getSelection()));
		return result;
	}
}

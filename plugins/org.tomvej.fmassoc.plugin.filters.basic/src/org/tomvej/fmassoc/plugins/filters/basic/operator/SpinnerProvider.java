package org.tomvej.fmassoc.plugins.filters.basic.operator;

import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;

/**
 * Creates spinner to provide {@link Integer} values.
 * 
 * @author Tomáš Vejpustek
 */
public class SpinnerProvider implements ValueControlProvider<Integer> {

	@Override
	public Control createControl(Composite parent, Consumer<Integer> listener, Integer initial) {
		Spinner input = new Spinner(parent, SWT.BORDER);
		if (initial == null) {
			initial = 0;
			listener.accept(0);
		}
		input.setValues(initial, Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 1, 10);
		input.addModifyListener(e -> listener.accept(input.getSelection()));
		return input;
	}
}
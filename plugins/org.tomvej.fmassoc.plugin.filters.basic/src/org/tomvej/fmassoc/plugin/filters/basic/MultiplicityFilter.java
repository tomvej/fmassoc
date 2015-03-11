package org.tomvej.fmassoc.plugin.filters.basic;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.function.Predicate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.tomvej.fmassoc.filter.Filter;
import org.tomvej.fmassoc.model.db.Multiplicity;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;

/**
 * Filter for multiplicity. Contains simple checkbox for each multiplicity.
 * 
 * @author Tomáš Vejpustek
 */
public class MultiplicityFilter implements Filter<Multiplicity> {
	private static final EnumMap<Multiplicity, String> FORMATTER = new EnumMap<>(Multiplicity.class);
	static {
		FORMATTER.put(Multiplicity.ONE_TO_ONE, "1:1");
		FORMATTER.put(Multiplicity.ONE_TO_MANY, "1:N");
		FORMATTER.put(Multiplicity.MANY_TO_ONE, "N:1");
		FORMATTER.put(Multiplicity.MANY_TO_MANY, "M:N");
	}

	private static String format(Multiplicity mult) {
		String result = FORMATTER.get(mult);
		return result != null ? result : mult.toString();
	}


	private final EnumSet<Multiplicity> multiplicities = EnumSet.noneOf(Multiplicity.class);


	@Override
	public Predicate<Multiplicity> getFilter() {
		return new Predicate<Multiplicity>() {
			@Override
			public boolean test(Multiplicity t) {
				return multiplicities.contains(t);
			}

			@Override
			public String toString() {
				if (multiplicities.isEmpty()) {
					return " : none";
				} else if (multiplicities.size() == 1) {
					return " : " + format(multiplicities.iterator().next());
				} else {
					StringBuilder result = new StringBuilder(" in (");
					multiplicities.forEach(m -> result.append(format(m)).append(", "));
					result.delete(result.length() - 2, result.length());
					return result.append(")").toString();
				}
			}
		};
	}

	@Override
	public Control createFilterPanel(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout());
		for (Multiplicity mult : Multiplicity.values()) {
			Button btn = new Button(container, SWT.CHECK);
			btn.setText(format(mult));
			btn.setSelection(multiplicities.contains(mult));
			btn.addSelectionListener(new SelectionWrapper(e -> {
				if (btn.getSelection()) {
					multiplicities.add(mult);
				} else {
					multiplicities.remove(mult);
				}
			}));
		}
		return container;
	}
}

package org.tomvej.fmassoc.filter.dialog.internal;

import java.util.function.Predicate;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;
import org.tomvej.fmassoc.filter.Filter;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.model.path.PathInfo;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;

/**
 * Instance of a filter for a path property.
 * 
 * @author Tomáš Vejpustek
 * @param <T>
 *            type of path property values
 */
public class FilterInstance<T> implements Predicate<Path> {
	private final PathPropertyEntry<T> property;
	private final Filter<T> filter;

	/**
	 * Specify path property and filter.
	 */
	public FilterInstance(PathPropertyEntry<T> property, Filter<T> filter) {
		this.property = Validate.notNull(property);
		this.filter = Validate.notNull(filter);
	}

	/**
	 * Create panel containing the filter visual component.
	 */
	public Composite createFilterPanel(Composite parent, Runnable refresher) {
		final Composite container = new Composite(parent, SWT.BORDER);
		container.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		container.setLayout(new GridLayout(3, false));

		Label propertyLbl = new Label(container, SWT.NONE);
		propertyLbl.setText(property.getName());
		propertyLbl.setToolTipText(property.getDescription());

		filter.createFilterPanel(container).setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		Button rmBtn = new Button(container, SWT.PUSH);
		rmBtn.setText("X");
		rmBtn.addSelectionListener(new SelectionWrapper(e -> {
			container.dispose();
			refresher.run();
		}));
		return container;
	}

	@Override
	public boolean test(Path t) {
		return filter.getFilter().test(property.getProperty().getValue(t));
	}


	/**
	 * Returns predicate which tests whether given path info satisfies this
	 * filter.
	 */
	public Predicate<PathInfo> getPruning() {
		return info -> !filter.getFilter().test(info.getProperty(property.getProperty()));
	}

	/**
	 * Returns used path property.
	 */
	public PathPropertyEntry<T> getProperty() {
		return property;
	}

	@Override
	public String toString() {
		return property.getName() + " " + filter.getFilter().toString();
	}
}

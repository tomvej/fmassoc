package org.tomvej.fmassoc.parts.paths.filter;

import java.util.function.Predicate;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.parts.paths.filterprovider.FilterPanel;
import org.tomvej.fmassoc.parts.paths.filterprovider.FilterProvider;

public class FilterRow<T> {
	private final Composite container;
	private final FilterPanel<T> filter;
	private final PathProperty<T> property;

	public FilterRow(Composite parent, PathPropertyEntry<T> pathProperty, FilterProvider<T> provider,
			final Runnable refresher) {
		property = pathProperty.getProperty();

		container = new Composite(parent, SWT.BORDER);
		container.setLayoutData(GridDataFactory.fillDefaults().create());
		container.setLayout(new GridLayout(3, false));

		Label propertyLbl = new Label(container, SWT.NONE);
		propertyLbl.setText(pathProperty.getName());
		propertyLbl.setToolTipText(pathProperty.getDescription());

		filter = provider.apply(container);

		Button rmBtn = new Button(container, SWT.PUSH);
		rmBtn.setText("X");
		rmBtn.addSelectionListener(new SelectionWrapper(e -> {
			dispose();
			refresher.run();
		}));
		refresher.run();
	}

	public boolean isDisposed() {
		return container.isDisposed();
	}

	public Predicate<Path> getFilter() {
		return p -> filter.getFilter().test(property.getValue(p));
	}

	public void dispose() {
		container.dispose();
	}
}

package org.tomvej.fmassoc.plugin.simplepruningfinder;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.tomvej.fmassoc.core.search.PathFinderProvider;

public class Part {
	@Inject
	private IEclipseContext context;

	private Spinner length;
	private Button optional, mn;

	@PostConstruct
	public void createComponents(Composite parent) {
		parent.setLayout(new RowLayout(SWT.VERTICAL));

		Composite lengthComposite = new Composite(parent, SWT.NONE);
		lengthComposite.setLayout(new GridLayout(2, false));
		GridDataFactory layout = GridDataFactory.fillDefaults().align(SWT.FILL, SWT.CENTER);

		Label lengthLbl = new Label(lengthComposite, SWT.NONE);
		lengthLbl.setLayoutData(layout.create());
		lengthLbl.setText("Length limit");

		length = new Spinner(lengthComposite, SWT.BORDER);
		length.setLayoutData(layout.create());
		length.setValues(0 /* TODO selection */, 0, 20, 0, 1, 5);
		// TODO there is a problem with width

		optional = new Button(parent, SWT.CHECK);
		optional.setText("Optional associations");

		mn = new Button(parent, SWT.CHECK);
		mn.setText("M:N associations");

	}

	private void refresh() {
		context.set(PathFinderProvider.class, new SimplePathFinderProvider(
				new Settings(optional.getSelection(), mn.getSelection(), length.getSelection(), 1)));
	}

}

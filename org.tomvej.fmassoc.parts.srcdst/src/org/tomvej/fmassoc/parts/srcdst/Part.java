package org.tomvej.fmassoc.parts.srcdst;

import javax.annotation.PostConstruct;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.tomvej.fmassoc.core.widgets.tablechooser.TableChooser;

public class Part {
	private TableChooser source;
	private DestinationChooser destination;

	@PostConstruct
	public void createComponents(Composite parent) {
		parent.setLayout(new GridLayout(2, true));

		GridDataFactory layout = GridDataFactory.fillDefaults().grab(true, true);

		Group grp = new Group(parent, SWT.SHADOW_ETCHED_OUT);
		grp.setText("Source table");
		grp.setLayoutData(layout.create());
		grp.setLayout(new GridLayout());
		source = new TableChooser(grp);
		source.setLayoutData(layout.create());

		destination = new DestinationChooser(parent);
		destination.setLayoutData(layout.create());
	}
}

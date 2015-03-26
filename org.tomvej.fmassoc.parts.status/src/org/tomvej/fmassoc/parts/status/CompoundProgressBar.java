package org.tomvej.fmassoc.parts.status;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ProgressBar;

public class CompoundProgressBar extends Composite {
	private final ProgressBar deterBar, indeterBar;
	private final GridData deterData, indeterData;

	public CompoundProgressBar(Composite parent, int style) {
		super(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		super.setLayout(layout);

		deterBar = new ProgressBar(this, style);
		indeterBar = new ProgressBar(this, style | SWT.INDETERMINATE);

		GridDataFactory data = GridDataFactory.fillDefaults().grab(true, true);
		deterData = data.create();
		deterBar.setLayoutData(deterData);
		indeterData = data.create();
		indeterBar.setLayoutData(indeterData);

		setDeterminate(true);
	}

	@Override
	public void setLayout(Layout layout) {
		// not allowed
	}


	public void setDeterminate(boolean determinate) {
		deterData.exclude = !determinate;
		deterBar.setVisible(determinate);
		indeterData.exclude = determinate;
		indeterBar.setVisible(!determinate);
		layout();
	}

}

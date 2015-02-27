package org.tomvej.fmassoc.parts.altsrcdst;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.core.communicate.DataModelTopic;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.parts.altsrcdst.srcdst.SourceDestinationPanel;

public class Part {
	private SourceDestinationPanel srcDst;

	@PostConstruct
	public void createComponents(Composite parent, Shell shell, @Optional DataModel model) {
		parent.setLayout(new GridLayout(2, true));

		GridDataFactory layout = GridDataFactory.fillDefaults().grab(true, true);

		srcDst = new SourceDestinationPanel(parent);
		srcDst.setLayoutData(layout.create());

		if (model != null) {
			srcDst.setTables(model.getTables());
		}
	}

	@Optional
	@Inject
	public void dataModelChanged(@UIEventTopic(DataModelTopic.MODEL_CHANGED) DataModel model) {
		if (srcDst != null) {
			srcDst.setTables(model.getTables());
		}
	}
}

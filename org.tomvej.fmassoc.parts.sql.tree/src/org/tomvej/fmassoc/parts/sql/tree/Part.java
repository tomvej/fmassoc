package org.tomvej.fmassoc.parts.sql.tree;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.parts.sql.tree.content.PathContentProvider;

public class Part {
	private CheckboxTreeViewer tree;

	@PostConstruct
	public void createComponents(Composite parent, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Path selected) {
		parent.setLayout(new GridLayout(2, false));

		Text result = new Text(parent, SWT.WRAP | SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		result.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).hint(0, 2 * result.getLineHeight()).span(2, 1)
				.create());
		result.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

		tree = new CheckboxTreeViewer(parent, SWT.BORDER);
		tree.setContentProvider(new PathContentProvider());
		tree.setLabelProvider(new PathTreeLabelProvider());
		tree.getTree().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(1, 8).create());


		for (String text : new String[] {
				"Print ID_OBJECTs",
				"Print associations",
				"Print properties",
				"Print version properties",
				"Abbreviate table names (i.e. T195)",
				"Prefix column names with table name",
				"Use LEFT JOIN" }) {
			Button btn = new Button(parent, SWT.CHECK);
			btn.setText(text);
		}

		pathSelected(selected);
	}

	@Inject
	public void pathSelected(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Path selected) {
		if (tree != null && !tree.getTree().isDisposed()) {
			tree.setInput(selected);
		}
	}


}

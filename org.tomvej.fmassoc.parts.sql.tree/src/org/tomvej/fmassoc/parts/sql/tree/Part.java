package org.tomvej.fmassoc.parts.sql.tree;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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
import org.tomvej.fmassoc.parts.sql.tree.check.PathTreeCheckModel;
import org.tomvej.fmassoc.parts.sql.tree.model.PathContentProvider;
import org.tomvej.fmassoc.parts.sql.tree.transform.Option;

public class Part {
	private CheckboxTreeViewer tree;
	private Map<Option, Button> options;

	@PostConstruct
	public void createComponents(Composite parent, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Path selected) {
		parent.setLayout(new GridLayout(2, false));

		Text result = new Text(parent, SWT.WRAP | SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		result.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).hint(0, 2 * result.getLineHeight()).span(2, 1)
				.create());
		result.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

		tree = new CheckboxTreeViewer(parent, SWT.BORDER);
		PathContentProvider provider = new PathContentProvider();
		tree.setContentProvider(provider);
		PathTreeCheckModel checkModel = new PathTreeCheckModel(tree, provider);
		tree.setLabelProvider(new PathTreeLabelProvider());
		tree.getTree().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(1, 8).create());

		options = Arrays.stream(Option.values()).collect(
				Collectors.toMap(Function.identity(), o -> createOptionButton(parent, o)));
		checkModel.setOidButton(options.get(Option.OIDS));
		checkModel.setAssociationButton(options.get(Option.ASSOC));
		checkModel.setPropertyButton(options.get(Option.PROPERTY));
		checkModel.setVersionButton(options.get(Option.VERSION));

		pathSelected(selected);
	}

	private Button createOptionButton(Composite parent, Option option) {
		Button result = new Button(parent, SWT.CHECK);
		result.setText(option.getMessage());
		return result;
	}

	@Inject
	public void pathSelected(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Path selected) {
		if (tree != null && !tree.getTree().isDisposed()) {
			tree.setInput(selected);
		}
	}


}

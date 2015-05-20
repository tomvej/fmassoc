package org.tomvej.fmassoc.parts.sql.tree;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.parts.sql.tree.check.PathTreeCheckModel;
import org.tomvej.fmassoc.parts.sql.tree.model.PathContentProvider;
import org.tomvej.fmassoc.parts.sql.tree.transform.Option;
import org.tomvej.fmassoc.parts.sql.tree.transform.TreeHandleFactory;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.transform.sql.formatters.JoinFormatter;

public class Part {
	private CheckboxTreeViewer tree;
	private Text output;
	private Map<Option, Button> options;
	private SelectionListener transformPath = new SelectionWrapper(e -> delayedTransformPath());

	private Path selected;

	@PostConstruct
	public void createComponents(Composite parent, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Path selected) {
		parent.setLayout(new GridLayout(2, false));

		output = new Text(parent, SWT.WRAP | SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		output.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).hint(0, 5 * output.getLineHeight()).span(2, 1)
				.create());
		output.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));

		tree = new CheckboxTreeViewer(parent, SWT.BORDER);
		PathContentProvider provider = new PathContentProvider();
		tree.setContentProvider(provider);
		PathTreeCheckModel checkModel = new PathTreeCheckModel(tree, provider);
		tree.setLabelProvider(new PathTreeLabelProvider());
		tree.getTree().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(1, 8).create());
		tree.addCheckStateListener(e -> delayedTransformPath());

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
		result.addSelectionListener(transformPath);
		return result;
	}

	@Inject
	private UISynchronize sync;

	private void delayedTransformPath() {
		sync.asyncExec(this::transformPath);
	}

	private void transformPath() {
		if (output == null || output.isDisposed()) {
			return;
		}
		String result = null;
		if (selected != null) {
			Set<Option> selectedOptions = options.entrySet().stream().filter(e -> e.getValue().getSelection())
					.map(e -> e.getKey()).collect(Collectors.toSet());
			TreeHandleFactory handleFactory = new TreeHandleFactory(tree, selectedOptions);
			result = new JoinFormatter(handleFactory, false, selectedOptions.contains(Option.LEFT_JOIN))
					.formatPath(selected);
		}
		// TODO send path if pinned
		output.setText(result != null ? result : "");
	}

	@Inject
	public void pathSelected(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Path selected) {
		this.selected = selected;
		if (tree != null && !tree.getTree().isDisposed()) {
			tree.setInput(selected);
			transformPath();
		}
	}
}

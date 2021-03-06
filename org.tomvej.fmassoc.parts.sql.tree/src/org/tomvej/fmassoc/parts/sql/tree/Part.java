package org.tomvej.fmassoc.parts.sql.tree;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.parts.sql.tree.model.PathContentProvider;
import org.tomvej.fmassoc.parts.sql.tree.transform.Option;
import org.tomvej.fmassoc.parts.sql.tree.transform.OptionHandleFactory;
import org.tomvej.fmassoc.parts.sql.tree.transform.TreeHandleFactory;
import org.tomvej.fmassoc.swt.wrappers.FocusGainedWrapper;
import org.tomvej.fmassoc.swt.wrappers.KeyReleasedWrapper;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.transform.sql.formatters.JoinFormatter;
import org.tomvej.fmassoc.transform.sql.handles.HandleFactory;

/**
 * Part which displays path as a tree of tables, association and properties
 * (grouped) and enables the user to specify which of to use in SELECT
 * statement.
 * 
 * @author Tomáš Vejpustek
 */
public class Part {
	private CheckboxTreeViewer tree;
	private Text output;
	private Map<Option, Button> options;
	private SelectionListener transformPath = new SelectionWrapper(e -> delayedTransformPath());
	private IEclipseContext context;

	private Path selected;
	private boolean pinned = false;

	/**
	 * Initialize components.
	 */
	@PostConstruct
	public void createComponents(Composite parent, @Optional @Named(IServiceConstants.ACTIVE_SELECTION) Path selected,
			MApplication app, MPart thisPart, @Optional @Named(ContextObjects.TRANSFORMATION_PART) MPart pinnedPart) {
		context = app.getContext();
		parent.setLayout(new GridLayout(2, false));

		output = new Text(parent, SWT.WRAP | SWT.BORDER | SWT.MULTI | SWT.READ_ONLY);
		output.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).hint(0, 2 * output.getLineHeight()).span(2, 1)
				.create());
		output.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		output.addFocusListener(new FocusGainedWrapper(e -> output.selectAll()));
		output.addKeyListener(new KeyReleasedWrapper('a', SWT.CTRL, e -> output.selectAll()));

		tree = new CheckboxTreeViewer(parent, SWT.BORDER);
		PathContentProvider provider = new PathContentProvider();
		tree.setContentProvider(provider);
		checkModel = new PathTreeCheckModel(tree, provider);
		tree.setLabelProvider(new PathTreeLabelProvider());
		tree.getTree().setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		tree.addCheckStateListener(e -> delayedTransformPath());

		Composite buttons = new Composite(parent, SWT.NONE);
		buttons.setLayoutData(GridDataFactory.fillDefaults().create());
		buttons.setLayout(new RowLayout(SWT.VERTICAL));

		options = Arrays.stream(Option.values()).collect(
				Collectors.toMap(Function.identity(), o -> createOptionButton(buttons, o, thisPart)));
		checkModel.setOidButton(options.get(Option.OIDS));
		checkModel.setAssociationButton(options.get(Option.ASSOC));
		checkModel.setPropertyButton(options.get(Option.PROPERTY));

		pinned = thisPart.equals(pinnedPart);
		pathSelected(selected);
	}

	private Button createOptionButton(Composite parent, Option option, MPart part) {
		Button result = new Button(parent, SWT.CHECK);
		result.setText(option.getMessage());
		result.addSelectionListener(transformPath);

		result.setSelection(part.getTags().contains(option.getTag()));
		result.addSelectionListener(new SelectionWrapper(e -> {
			if (result.getSelection()) {
				part.getTags().add(option.getTag());
			} else {
				part.getTags().remove(option.getTag());
			}
		}));
		return result;
	}

	@Inject
	private UISynchronize sync;
	private PathTreeCheckModel checkModel;

	private void delayedTransformPath() {
		sync.asyncExec(() -> transformPath(false));
	}

	private void transformPath(boolean pathChanged) {
		if (output == null || output.isDisposed()) {
			return;
		}
		String result = null;
		if (selected != null) {
			Set<Option> selectedOptions = options.entrySet().stream().filter(e -> e.getValue().getSelection())
					.map(e -> e.getKey()).collect(Collectors.toSet());
			boolean allSelected = !selectedOptions.contains(Option.PREFIX_COL) &&
					selected.getTables().stream().allMatch(t -> tree.getChecked(t) && !tree.getGrayed(t));
			HandleFactory handles = pathChanged ?
					new OptionHandleFactory(selectedOptions) :
					new TreeHandleFactory(tree, selectedOptions);
			result = new JoinFormatter(handles, allSelected,
					selectedOptions.contains(Option.LEFT_JOIN)).formatPath(selected);
		}

		if (pinned) {
			context.set(ContextObjects.TRANSFORMED_PATH, result);
		}
		output.setText(result != null ? result : "");
	}

	/**
	 * Listen for path selection change.
	 */
	@Inject
	public void pathSelected(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Path selected) {
		this.selected = selected;
		if (tree != null && !tree.getTree().isDisposed()) {
			tree.setInput(selected);
			checkModel.refresh();
			transformPath(true);
		}
	}

	/**
	 * Listen for part pinning.
	 */
	@Inject
	public void partPinned(@Optional @Named(ContextObjects.TRANSFORMATION_PART) MPart otherPart, MPart thisPart) {
		pinned = thisPart.equals(otherPart);
		if (pinned) {
			transformPath(false);
		}
	}
}

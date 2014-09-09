package org.tomvej.fmassoc.parts.sql.independent;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.di.PersistState;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.osgi.service.prefs.BackingStoreException;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.transform.sql.formatters.JoinFormatter;

public class Part {
	@Inject
	private Logger logger;
	@Inject
	@Preference("org.tomvej.fmassoc.parts.sql.independent")
	private IEclipsePreferences preference;

	private Map<Options, Button> options;
	private Path selected;
	private Text output;

	@PostConstruct
	public void createComponents(Composite parent,
			@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Path path) {
		parent.setLayout(new GridLayout(1, false));

		output = new Text(parent, SWT.MULTI | SWT.READ_ONLY | SWT.BORDER | SWT.WRAP);
		output.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_LIST_BACKGROUND));
		output.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).hint(0, 3 * output.getLineHeight()).create());

		Composite optionPanel = new Composite(parent, SWT.NONE);
		optionPanel.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).create());
		optionPanel.setLayout(new RowLayout(SWT.VERTICAL));

		options = Arrays.asList(Options.values()).stream()
				.collect(Collectors.toMap(Function.identity(), o -> createOptionButton(optionPanel, o)));
		selected = path;
	}

	private Button createOptionButton(Composite parent, Options option) {
		Button result = new Button(parent, SWT.CHECK);
		result.setText(option.getMessage());
		result.setSelection(preference.getBoolean(option.getPropertyKey(), option.isDefaultSelected()));
		result.addSelectionListener(new SelectionWrapper(
				e -> preference.putBoolean(option.getPropertyKey(), result.getSelection())));
		result.addSelectionListener(new SelectionWrapper(e -> transformPath()));
		return result;
	}

	@PersistState
	public void save() {
		try {
			preference.flush();
		} catch (BackingStoreException bse) {
			logger.error(bse, "Unable to store selected options.");
		}
	}

	@Inject
	public void pathSelected(@Optional @Named(IServiceConstants.ACTIVE_SELECTION) Path path) {
		selected = path;
		transformPath();
	}

	private void transformPath() {
		if (output == null || output.isDisposed()) {
			return;
		}
		if (selected != null) {
			IndependentHandleFactory factory = new IndependentHandleFactory(
					options.keySet().stream().filter(o -> options.get(o).getSelection()).collect(Collectors.toSet()),
					selected.getSource(), selected.getDestination());
			String result = new JoinFormatter(factory, factory.displayAllColumns()).formatPath(selected);
			output.setText(result);
		} else {
			output.setText("");
		}
	}
}

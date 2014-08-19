package org.tomvej.fmassoc.parts.model.core;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.core.wrappers.TextLabelProvider;
import org.tomvej.fmassoc.parts.model.ModelLoader;

/**
 * Toolbar widget used to switch data models.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class ModelChooser {
	@Inject
	private Logger logger;
	private ModelList models;
	private ComboViewer switcher;

	/**
	 * Create components comprising this widget.
	 */
	@PostConstruct
	public void createComponents(Composite parent, Shell parentShell, IExtensionRegistry registry, MApplication app,
			@Preference("org.tomvej.fmassoc.parts.model.models") IEclipsePreferences modelPreference) {

		// load model loaders and put them into context
		List<ModelLoaderEntry> loaders = loadModelLoaders(registry, parentShell);
		app.getContext().set(Constants.MODEL_LOADER_REGISTRY, loaders);

		// read models and put them into context
		PreferenceModelManager manager = new PreferenceModelManager(modelPreference, logger);
		List<ModelEntry> modelEntries = new ArrayList<>();
		IStatus status = manager.loadModels(modelEntries, loaders);
		if (!status.equals(Status.OK_STATUS)) {
			ErrorDialog.openError(parentShell, "Unaccessible Models", "Some models could not be loaded.", status);
		}
		models = new ModelList(manager, modelEntries);
		app.getContext().set(ModelList.class, models);

		switcher = new ComboViewer(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
		switcher.setContentProvider(new ObservableListContentProvider());
		switcher.setLabelProvider(new TextLabelProvider<ModelEntry>(entry -> entry.getLabel()));
		switcher.setInput(models);
		switcher.addSelectionChangedListener(event -> loadSelectedModel());
	}

	private List<ModelLoaderEntry> loadModelLoaders(IExtensionRegistry registry, Shell parent) {
		List<ModelLoaderEntry> result = new ArrayList<>();
		MultiStatus status = new MultiStatus(Constants.PLUGIN_ID, IStatus.OK, null, null);
		for (IConfigurationElement config : registry
				.getConfigurationElementsFor("org.tomvej.fmassoc.parts.model.modelLoader")) {
			String name = config.getAttribute("name");
			try {
				result.add(new ModelLoaderEntry(name, config.getAttribute("description"),
						(ModelLoader) config.createExecutableExtension("class")));
			} catch (CoreException ce) {
				String message = "Cannot load model loader" + name + " [" + config.getAttribute("class") + "].";
				logger.error(ce, message);
				status.add(new Status(IStatus.WARNING, Constants.PLUGIN_ID, message, ce));
			}
		}
		if (status.getChildren().length != 0) {
			ErrorDialog.openError(parent, "Unaccessible Model Loaders",
					"Some model loaders could not be loaded. Consequently, it might not be possible to load some models.",
					status);
		}
		return result;
	}

	private void loadSelectedModel() {
		// FIXME
	}
}

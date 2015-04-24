package org.tomvej.fmassoc.parts.model.core;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.services.log.Logger;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

/**
 * Manager of the model store.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class PreferenceModelManager {
	private final IEclipsePreferences preferences;
	private final Logger logger;

	private static String getLoaderId(ModelLoaderEntry loader) {
		return loader.getLoader().getClass().getCanonicalName();
	}

	private static Map<String, ModelLoaderEntry> prepareLoaderMap(List<ModelLoaderEntry> loaders) {
		return loaders.stream().collect(Collectors.toMap(PreferenceModelManager::getLoaderId, Function.identity()));
	}

	private Preferences getLoaders() {
		return preferences.node("loaders");
	}

	private Preferences getLabels() {
		return preferences.node("labels");
	}

	/**
	 * Specify preference storage.
	 */
	public PreferenceModelManager(IEclipsePreferences preference, Logger logger) {
		this.preferences = Validate.notNull(preference);
		this.logger = Validate.notNull(logger);
	}

	/**
	 * Remove model from storage.
	 */
	public void remove(ModelEntry model) {
		String id = Validate.notNull(model).getId();
		getLoaders().remove(id);
		getLabels().remove(id);
		try {
			preferences.flush();
			logger.info("Model removed: " + model);
		} catch (BackingStoreException bse) {
			logger.error(bse, "Unable to remove model: " + model);
		}
	}

	/**
	 * Add new model to storage. Generates internal id (
	 * {@link ModelEntry#getId()}). The actual configuration may not be
	 * specified yet.
	 * 
	 * @param label
	 *            Descriptive label of the model ({@link ModelEntry#getLabel()}.
	 * @param loader
	 *            Loader used to load the model.
	 * @return The newly created model entry, {@code null} if it could not be
	 *         created.
	 */
	public ModelEntry add(String label, ModelLoaderEntry loader) {
		String id = getUniqueId();
		if (id == null) {
			logger.error("Cannot generate id for model " + label + " [" + loader + "]");
			return null;
		}

		ModelEntry result = new ModelEntry(id, label, loader);
		getLoaders().put(id, getLoaderId(loader));
		getLabels().put(id, label);
		try {
			preferences.flush();
			logger.info("Model added: " + result);
			return result;
		} catch (BackingStoreException bse) {
			logger.error(bse, "Unable to add model: " + result);
			// rollback
			getLoaders().remove(id);
			getLabels().remove(id);
			return null;
		}
	}

	private String getUniqueId() {
		for (int tries = 10; tries > 0; tries--) {
			String id = UUID.randomUUID().toString();
			if (getLoaders().get(id, null) == null) {
				return id;
			}
		}
		return null;
	}

	/**
	 * Load models from configuration.
	 * 
	 * @param target
	 *            List to add loaded models.
	 * @param modelLoaders
	 *            List of available model loaders.
	 * @return {@link Status#OK_STATUS} when loading was successful, status
	 *         containing errors otherwise.
	 */
	public IStatus loadModels(List<ModelEntry> target, List<ModelLoaderEntry> modelLoaders) {
		Validate.notNull(target);

		String[] ids;
		try {
			ids = getLoaders().keys();
		} catch (BackingStoreException bse) {
			String message = "Cannot get the list of stored models.";
			logger.error(bse, message);
			return new Status(IStatus.ERROR, Constants.PLUGIN_ID, message, bse);
		}

		Map<String, ModelLoaderEntry> loadersById = prepareLoaderMap(modelLoaders);

		MultiStatus status = new MultiStatus(Constants.PLUGIN_ID, IStatus.OK, "", null);
		for (String id : ids) {
			String label = getLabels().get(id, "");
			String loaderId = getLoaders().get(id, null);
			ModelLoaderEntry loader = loadersById.get(loaderId);
			if (loader == null) {
				String message = "Unable to load model " + label + "(" + id + "): There is no associated model loader "
						+ loaderId;
				logger.error(message);
				status.add(new Status(IStatus.WARNING, Constants.PLUGIN_ID, message));
			}
			target.add(new ModelEntry(id, label, loader));
		}

		if (status.getChildren().length == 0) {
			return Status.OK_STATUS;
		} else {
			return status;
		}
	}

}

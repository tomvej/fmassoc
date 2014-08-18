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
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

public class PreferenceModelManager {
	private final Preferences loaders, labels;

	private static String getLoaderId(ModelLoaderEntry loader) {
		return loader.getLoader().getClass().getCanonicalName();
	}

	private static Map<String, ModelLoaderEntry> prepareLoaderMap(List<ModelLoaderEntry> loaders) {
		return loaders.stream().collect(Collectors.toMap(PreferenceModelManager::getLoaderId, Function.identity()));
	}

	public PreferenceModelManager(IEclipsePreferences preference) {
		Validate.notNull(preference);
		loaders = preference.node("loaders");
		labels = preference.node("labels");
	}

	public void remove(ModelEntry model) {
		String id = Validate.notNull(model).getId();
		loaders.remove(id);
		labels.remove(id);
	}

	public ModelEntry add(String label, ModelLoaderEntry loader) {
		String id = getUniqueId();
		if (id == null) {
			return null;
		}

		ModelEntry result = new ModelEntry(id, label, loader);
		loaders.put(id, getLoaderId(loader));
		labels.put(id, label);
		return result;
	}

	private String getUniqueId() {
		for (int tries = 10; tries > 0; tries--) {
			String id = UUID.randomUUID().toString();
			if (loaders.get(id, null) == null) {
				return id;
			}
		}
		return null;
	}

	public IStatus loadModels(List<ModelEntry> target, List<ModelLoaderEntry> modelLoaders) {
		Validate.notNull(target);

		String[] ids;
		try {
			ids = loaders.keys();
		} catch (BackingStoreException bse) {
			return new Status(IStatus.ERROR, Constants.PLUGIN_ID, "Cannot get the list of stored models.", bse);
		}

		Map<String, ModelLoaderEntry> loadersById = prepareLoaderMap(modelLoaders);

		MultiStatus status = new MultiStatus(Constants.PLUGIN_ID, IStatus.OK, "Some models cannot be loaded.", null);
		for (String id : ids) {
			String label = labels.get(id, "");
			String loaderId = loaders.get(id, null);
			ModelLoaderEntry loader = loadersById.get(loaderId);
			if (loader == null) {
				status.add(new Status(IStatus.WARNING, Constants.PLUGIN_ID,
						"Unable to load model " + label + "(" + id + "): There is no associated model loader " + loaderId));
			} else {
				target.add(new ModelEntry(id, label, loader));
			}
		}

		if (status.getChildren().length == 0) {
			return Status.OK_STATUS;
		} else {
			return status;
		}
	}

}

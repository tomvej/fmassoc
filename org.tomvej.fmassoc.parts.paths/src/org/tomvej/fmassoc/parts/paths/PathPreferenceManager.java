package org.tomvej.fmassoc.parts.paths;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Manages preferences for found paths part.
 * 
 * @author Tomáš Vejpustek
 */
public class PathPreferenceManager {
	private static final String LABEL_PROVIDER = "label.provider";

	@Inject
	@Preference("org.tomvej.fmassoc.parts.paths")
	private IEclipsePreferences preference;
	@Inject
	private IEventBroker broker;
	@Inject
	private Logger logger;

	private Map<String, LabelProviderEntry> labelProviders;

	/**
	 * Load path preferences and put the manager into context.
	 */
	@PostConstruct
	public void load(IEclipseContext context, IExtensionRegistry extensions) {
		context.set(PathPreferenceManager.class, this);

		// load label providers
		labelProviders = Arrays
				.asList(extensions.getConfigurationElementsFor("org.tomvej.fmassoc.parts.paths.pathlabelprovider")).
				stream().map(c -> new LabelProviderEntry(c)).collect(Collectors.toMap(e -> e.getId(), Function.identity()));

		preference.addPreferenceChangeListener(this::preferenceChanged);
	}

	private void preferenceChanged(PreferenceChangeEvent event) {
		if (event.getNode().equals(preference)) { // main node
			if (event.getKey().equals(LABEL_PROVIDER)) {
				broker.post(PathTablePreferenceTopic.PROVIDER_CHANGE, getLabelProvider());
			}
		}
	}

	/**
	 * Saves the used path column label provider.
	 */
	public void setLabelProvider(LabelProviderEntry entry) {
		preference.put(LABEL_PROVIDER, entry != null ? entry.getId() : "");
		try {
			preference.flush();
		} catch (BackingStoreException bse) {
			logger.error(bse, "Unable to store path label provider preference.");
		}
	}

	/**
	 * Returns the used path column label provider entry.
	 * 
	 * @return Label provider or {@code null} when it was not found.
	 */
	public LabelProviderEntry getLabelProviderEntry() {
		String id = preference.get(LABEL_PROVIDER, null);
		if (StringUtils.isBlank(id)) {
			return null;
		}
		LabelProviderEntry result = labelProviders.get(id);
		if (result == null) {
			logger.error("Label provider " + id + " does not exist.");
		}
		return result;
	}

	/**
	 * Creates currently used path column label provider.
	 * 
	 * @return Label provider or null when it cannot be created.
	 */
	public ColumnLabelProvider getLabelProvider() {
		LabelProviderEntry entry = getLabelProviderEntry();
		if (entry != null) {
			try {
				return entry.create();
			} catch (CoreException ce) {
				logger.error(ce, "Cannot load label provider " + entry.getId());
			}
		}
		return null;
	}
}
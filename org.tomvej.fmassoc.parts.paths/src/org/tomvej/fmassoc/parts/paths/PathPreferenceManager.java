package org.tomvej.fmassoc.parts.paths;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;

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
	@Inject
	@Named(ContextObjects.PATH_PROPERTIES)
	private List<PathPropertyEntry<?>> pathProperties;

	private boolean dirty = false;

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
	}

	/**
	 * Saves the used path column label provider.
	 */
	public void setLabelProvider(LabelProviderEntry entry) {
		if (!Objects.equals(getLabelProviderEntry(), entry)) {
			preference.put(LABEL_PROVIDER, entry != null ? entry.getId() : "");
			makeDirty();
			broker.post(PathTablePreferenceTopic.PROVIDER_CHANGE, getLabelProvider(entry));
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

	private ColumnLabelProvider getLabelProvider(LabelProviderEntry entry) {
		if (entry != null) {
			try {
				return entry.create();
			} catch (CoreException ce) {
				logger.error(ce, "Cannot load label provider " + entry.getId());
			}
		}
		return null;
	}

	/**
	 * Creates currently used path column label provider.
	 * 
	 * @return Label provider or null when it cannot be created.
	 */
	public ColumnLabelProvider getLabelProvider() {
		return getLabelProvider(getLabelProviderEntry());
	}

	/**
	 * Returns unmodifiable list of available label providers.
	 */
	public Collection<LabelProviderEntry> getLabelProviders() {
		return Collections.unmodifiableCollection(labelProviders.values());
	}

	/**
	 * Returns whether path table preferences have been changed and not stored
	 * yet.
	 * 
	 * @return {@code true} if there are unstored preference changes,
	 *         {@code false} otherwise.
	 */
	public boolean isDirty() {
		return dirty;
	}

	private void makeDirty() {
		dirty = true;
	}

	/**
	 * Tries storing preference changes.
	 * 
	 * @return {@code true} if the operation was successful, {@code false}
	 *         otherwise.
	 */
	public boolean store() {
		if (isDirty()) {
			try {
				preference.flush();
				dirty = false;
				logger.info("Path table preferences stored.");
			} catch (BackingStoreException bse) {
				logger.error(bse, "Cannot store path table preferences.");
			}
		}
		return !isDirty();
	}

	private Preferences getColumnsPreferences() {
		return preference.node("columns");
	}

	private boolean isColumnVisible(PathPropertyEntry<?> property) {
		return getColumnsPreferences().getBoolean(property.getId(), true);
	}

	/**
	 * Return all visible path property columns.
	 */
	public Collection<PathPropertyEntry<?>> getColumns() {
		return pathProperties.stream().filter(this::isColumnVisible).collect(Collectors.toList());

	}

	/**
	 * Add path property column.
	 */
	public void addColumn(PathPropertyEntry<?> column) {
		boolean set = getColumnsPreferences().get(column.getId(), null) != null;
		if (!isColumnVisible(column) || !set) {
			getColumnsPreferences().putBoolean(column.getId(), true);
			makeDirty();
			if (set) {
				broker.send(PathTablePreferenceTopic.COLUMN_ADDED, column);
			}
		}
	}

	/**
	 * Remove path property column.
	 */
	public void removeColumn(PathPropertyEntry<?> column) {
		if (isColumnVisible(column)) {
			getColumnsPreferences().putBoolean(column.getId(), false);
			makeDirty();
			broker.send(PathTablePreferenceTopic.COLUMN_REMOVED, column);
		}
	}

}
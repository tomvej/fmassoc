package org.tomvej.fmassoc.plugin.mobilemodelloader;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Defines how model preferences are stored.
 * 
 * @author Tomáš Vejpustek
 */
public class ModelStorage {
	private final IEclipsePreferences preference;

	/**
	 * Specify model id.
	 */
	public ModelStorage(String id) {
		preference = InstanceScope.INSTANCE.getNode("org.tomvej.fmassoc.plugin.mobilemodelloader." + id);
	}

	/**
	 * Specify path to model file.
	 */
	public void setFile(String path) {
		preference.put("path", path);
	}

	/**
	 * Retrieve path to model file.
	 */
	public String getFile() {
		return preference.get("path", null);
	}

	private Preferences forbidden() {
		return preference.node("forbidden");
	}

	/**
	 * Specify default forbidden tables.
	 */
	public void setForbiddenTables(Collection<Table> tables) throws BackingStoreException {
		setForbidden(tables.stream().map(t -> t.getName()));
	}

	/**
	 * Specify default forbidden tables by their names.
	 */
	public void setForbidden(Collection<String> names) throws BackingStoreException {
		setForbidden(names.stream());
	}

	private void setForbidden(Stream<String> names) throws BackingStoreException {
		forbidden().clear();
		names.forEach(n -> forbidden().put(n, ""));
	}

	/**
	 * Return forbidden tables names.
	 */
	public Collection<String> getForbidden() throws BackingStoreException {
		return Arrays.asList(forbidden().keys());
	}

	/**
	 * Store preference changes to disk.
	 */
	public void store() throws BackingStoreException {
		preference.flush();
	}

}

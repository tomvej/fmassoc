package org.tomvej.fmassoc.test.modelloader;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

/**
 * Stores preferences for testing models.
 * 
 * @author Tomáš Vejpustek
 */
public class ModelStorage {
	private final IEclipsePreferences preference;

	/**
	 * Specify model id.
	 */
	public ModelStorage(String id) {
		preference = InstanceScope.INSTANCE.getNode("org.tomvej.fmasssoc.test.modelloader." + id);
	}

	/**
	 * Return whether model loading should always fail.
	 */
	public boolean fails() {
		return preference.getBoolean("fails", false);
	}

	/**
	 * Specify whether model loading should always fail.
	 */
	public void setFails(boolean fails) {
		preference.putBoolean("fails", fails);
	}

	/**
	 * Return duration of model loading.
	 */
	public long getDuration() {
		return preference.getLong("duration", 0l);
	}

	/**
	 * Specify duration of model loading.
	 */
	public void setDuration(long duration) {
		preference.putLong("duration", duration);
	}

	/**
	 * Attempt to store model preference.
	 */
	public void store() throws BackingStoreException {
		preference.flush();
	}

}

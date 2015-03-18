package org.tomvej.fmassoc.test.modelloader;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.osgi.service.prefs.BackingStoreException;

public class ModelStorage {
	private final IEclipsePreferences preference;

	public ModelStorage(String id) {
		preference = InstanceScope.INSTANCE.getNode("org.tomvej.fmasssoc.test.modelloader." + id);
	}

	public boolean fails() {
		return preference.getBoolean("fails", false);
	}

	public void setFails(boolean fails) {
		preference.putBoolean("fails", fails);
	}

	public long getDuration() {
		return preference.getLong("duration", 0l);
	}

	public void setDuration(long duration) {
		preference.putLong("duration", duration);
	}

	public void store() throws BackingStoreException {
		preference.flush();
	}

}

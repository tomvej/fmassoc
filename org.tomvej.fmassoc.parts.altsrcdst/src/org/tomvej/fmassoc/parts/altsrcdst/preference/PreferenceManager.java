package org.tomvej.fmassoc.parts.altsrcdst.preference;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MAddon;

/**
 * Utility class for accessing source and destination chooser preference.
 * 
 * @author Tomáš Vejpustek
 */
public class PreferenceManager {
	private static final String DISPLAY_IMPL_NAME_KEY = "displayImplName";

	@Inject
	private IEventBroker broker;
	@Inject
	private MAddon addon;

	/**
	 * Load this object into context.
	 */
	@PostConstruct
	public void load(IEclipseContext context) {
		context.set(PreferenceManager.class, this);
	}

	/**
	 * Return how tables should be displayed.
	 */
	public PopupDisplayProperty getDisplayProperty() {
		if (Boolean.valueOf(addon.getPersistedState().get(DISPLAY_IMPL_NAME_KEY))) {
			return PopupDisplayProperty.IMPL_NAME;
		} else {
			return PopupDisplayProperty.NAME;
		}
	}

	/**
	 * Set how tables should be displayed.
	 */
	public void setDisplayProperty(PopupDisplayProperty type) {
		addon.getPersistedState().put(DISPLAY_IMPL_NAME_KEY, Boolean.toString(type.equals(PopupDisplayProperty.IMPL_NAME)));
		broker.post(PreferenceTopic.DISPLAY_PROPERTY_CHANGE, type);
	}
}

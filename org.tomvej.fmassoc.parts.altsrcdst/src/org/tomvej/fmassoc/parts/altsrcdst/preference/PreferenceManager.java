package org.tomvej.fmassoc.parts.altsrcdst.preference;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.model.application.MAddon;


public class PreferenceManager {
	private static final String DISPLAY_IMPL_NAME_KEY = "displayImplName";

	@Inject
	private IEventBroker broker;
	@Inject
	private MAddon addon;

	@PostConstruct
	public void load(IEclipseContext context) {
		context.set(PreferenceManager.class, this);

	}

	public PopupDisplayProperty getDisplayProperty() {
		if (Boolean.valueOf(addon.getPersistedState().get(DISPLAY_IMPL_NAME_KEY))) {
			return PopupDisplayProperty.IMPL_NAME;
		} else {
			return PopupDisplayProperty.NAME;
		}
	}

	public void setDisplayProperty(PopupDisplayProperty type) {
		boolean implName = false;
		switch (type) {
			case IMPL_NAME:
				implName = true;
				break;
		}
		addon.getPersistedState().put(DISPLAY_IMPL_NAME_KEY, Boolean.toString(implName));
		broker.post(PreferenceTopic.DISPLAY_PROPERTY_CHANGE, type);
	}
}

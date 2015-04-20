package org.tomvej.fmassoc.plugin.pinsqltransform;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MAddon;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.tomvej.fmassoc.core.communicate.PathTransformerTopic;

public class PersistTransformationPart {
	private static final String KEY_SELECTED_TRANSFORMER = "selected_transformer";

	@Inject
	private MAddon addon;

	@PostConstruct
	public void retrievePinnedState(EModelService models, MApplication app, IEventBroker broker, UISynchronize sync) {
		String transformer = addon.getPersistedState().get(KEY_SELECTED_TRANSFORMER);
		if (transformer != null) {
			List<MPart> parts = models.findElements(app, transformer, MPart.class, null);
			if (parts.size() == 1) {
				sync.asyncExec(() -> broker.post(PathTransformerTopic.SELECT, parts.get(0)));
			}
		}
	}

	@Inject
	@Optional
	public void transformerSelected(@EventTopic(PathTransformerTopic.SELECT) MPart part) {
		addon.getPersistedState().put(KEY_SELECTED_TRANSFORMER, part != null ? part.getElementId() : null);
	}

}

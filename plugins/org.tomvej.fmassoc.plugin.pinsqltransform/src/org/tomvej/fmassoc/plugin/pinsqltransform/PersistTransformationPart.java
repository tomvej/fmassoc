package org.tomvej.fmassoc.plugin.pinsqltransform;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.MAddon;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.tomvej.fmassoc.core.communicate.ContextObjects;

/**
 * Used to store and retrieve pinned SQL transformation part.
 * 
 * @author Tomáš Vejpustek
 */
public class PersistTransformationPart {
	private static final String KEY_SELECTED_TRANSFORMER = "selected_transformer";

	private MAddon addon;

	/**
	 * Retrieves pinned SQL transformation part and puts it into context (
	 * {@link ContextObjects#TRANSFORMATION_PART}).
	 */
	@Inject
	public PersistTransformationPart(EModelService models, MApplication app, IEclipseContext context, MAddon addon) {
		this.addon = addon;
		String transformer = addon.getPersistedState().get(KEY_SELECTED_TRANSFORMER);
		if (transformer != null) {
			List<MPart> parts = models.findElements(app, transformer, MPart.class, null);
			if (parts.size() == 1) {
				context.set(ContextObjects.TRANSFORMATION_PART, parts.get(0));
			}
		}
	}


	/**
	 * Stores pinned SQL transformation part.
	 */
	@Inject
	public void transformerSelected(@Optional @Named(ContextObjects.TRANSFORMATION_PART) MPart part) {
		addon.getPersistedState().put(KEY_SELECTED_TRANSFORMER, part != null ? part.getElementId() : null);
	}

}

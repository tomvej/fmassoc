package org.tomvej.fmassoc.partmanager;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

/**
 * Handler for part closing.
 * 
 * @author Tomáš Vejpustek
 */
public class CloseView {

	/**
	 * Marks all parts closable.
	 */
	@PostConstruct
	public void initialize(EModelService modelService, MTrimmedWindow window) {
		modelService.findElements(window, null, MPerspective.class, null).stream()
				.flatMap(p -> p.getChildren().stream())
				.flatMap(e -> modelService.findElements(e, null, MPart.class, null).stream())
				.forEach(p -> p.setCloseable(true));
	}

	/**
	 * Closes part by id.
	 */
	@Execute
	public void execute(EPartService partService, Logger logger,
			@Named("org.tomvej.fmassoc.partmanager.command.closeview.partid") String partId) {
		MPart part = partService.findPart(partId);
		if (part != null) {
			partService.hidePart(part);
		} else {
			logger.warn("View " + partId + " not found, cannot be closed.");
		}
	}
}

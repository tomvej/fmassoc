package org.tomvej.fmassoc.partmanager;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class CloseView {
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

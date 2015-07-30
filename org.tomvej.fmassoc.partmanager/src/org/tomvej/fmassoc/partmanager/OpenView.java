package org.tomvej.fmassoc.partmanager;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;

public class OpenView {

	@Execute
	public void execute(EPartService partService, Logger logger,
			@Named("org.tomvej.fmassoc.partmanager.command.openview.partid") String id) {
		MPart part = partService.findPart(id);
		if (part != null) {
			partService.activate(part);
		} else {
			logger.warn("View " + id + " not found, cannot be opened.");
		}
	}
}

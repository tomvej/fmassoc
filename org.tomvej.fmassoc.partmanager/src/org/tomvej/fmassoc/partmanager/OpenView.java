package org.tomvej.fmassoc.partmanager;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;

/**
 * Handler for part opening.
 * 
 * @author Tomáš Vejpustek
 */
public class OpenView {

	/**
	 * Opens part by id.
	 */
	@Execute
	public void execute(EPartService partService, Logger logger,
			@Named("org.tomvej.fmassoc.partmanager.command.openview.partid") String id) {
		System.err.println("Open view!");
		MPart part = partService.findPart(id);
		if (part != null) {
			partService.showPart(part, PartState.VISIBLE);
		} else {
			logger.warn("View " + id + " not found, cannot be opened.");
		}
	}
}

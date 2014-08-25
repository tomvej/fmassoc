package org.tomvej.fmassoc.parts.model.manager;

import java.util.Collections;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.services.log.Logger;

/**
 * Creates and displays dialog model manager.
 * 
 * @author Tomáš Vejpustek
 * @see ModelManagerDialog
 *
 */
public class ShowModelManager {
	@Inject
	private Logger logger;

	/**
	 * Create and display model manager.
	 */
	@Execute
	public void execute(IEclipseContext context, ECommandService commandService, EHandlerService handlerService) {
		ModelManagerDialog dialog = ContextInjectionFactory.make(ModelManagerDialog.class, context);
		dialog.open();

		// reload the model
		handlerService.executeHandler(
				commandService.createCommand("org.tomvej.fmassoc.parts.model.command.loadmodel", Collections.emptyMap()));
	}
}
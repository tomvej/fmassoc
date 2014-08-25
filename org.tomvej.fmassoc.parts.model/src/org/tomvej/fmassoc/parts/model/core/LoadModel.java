package org.tomvej.fmassoc.parts.model.core;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.core.communicate.DataModelTopic;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;

/**
 * Handler for model loading.
 * 
 * @author Tomáš Vejpustek
 */
public class LoadModel {
	@Inject
	private IEclipseContext context;
	@Inject
	private IEventBroker events;
	@Inject
	private Logger logger;

	/**
	 * Loads model the currently selected model.
	 */
	@Execute
	public void execute(ModelEntry current, Shell shell) {
		try {
			DataModel model = current.load();
			dataModelChanged(model);
			logger.info("Model loaded: " + current);
		} catch (ModelLoadingException mle) {
			dataModelChanged(null);
			logger.error(mle, "Unable to load model: " + current);
			MessageDialog.openError(shell, "Cannot load model",
					"Unable to load model " + current.getDescription() + ":" + mle.getLocalizedMessage());
			context.set(ModelEntry.class, null);
		}
	}

	private void dataModelChanged(DataModel model) {
		context.set(DataModel.class, model);
		events.post(DataModelTopic.MODEL_CHANGED, model);
	}

	/**
	 * Checks whether a model is selected.
	 * 
	 * @return {@code true} when a model is selected and can be loaded,
	 *         {@code false} otherwise.
	 */
	@CanExecute
	public boolean canExecute(@Optional ModelEntry current) {
		return current != null;
	}
}
package org.tomvej.fmassoc.parts.model.core;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.Preference;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.core.services.log.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.BusyIndicator;
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
	public void execute(ModelEntry current, Shell shell,
			@Preference(nodePath = Constants.PLUGIN_ID, value = Constants.LOADING_TIMEOUT) Long timeout,
			@Named(Constants.MODEL_ERRORS) Map<ModelEntry, ModelLoadingException> errors) {
		events.send(DataModelTopic.MODEL_LOADING, current.getLabel());

		Thread uiThread = Thread.currentThread(); // runs timeout process
		/* model loading job */
		Job loader = new Job("Loading model " + current) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					long time = System.currentTimeMillis();
					DataModel model = current.load(); // load model
					time = System.currentTimeMillis() - time;

					if (monitor.isCanceled()) // timeout has happened
						return Status.CANCEL_STATUS;

					uiThread.interrupt(); // interrupt timeout process
					dataModelChanged(model);
					logger.info("Model loaded in " + time + " ms: " + current);
					errors.remove(current);
					return Status.OK_STATUS;
				} catch (ModelLoadingException mle) {
					if (!monitor.isCanceled()) { // timeout has not happened
						uiThread.interrupt(); // interrupt timeout process
						dataModelChanged(null);
						context.set(ModelEntry.class, null);

						String message = "Unable to load model " + current.getDescription() + ": "
								+ mle.getLocalizedMessage();
						errors.put(current, mle);
						shell.getDisplay().asyncExec(() -> MessageDialog.openError(shell, "Cannot load model", message));
					}
					// return error status which is logged (automatically)
					return new Status(IStatus.ERROR, Constants.PLUGIN_ID, "Unable to load model: " + current, mle);
				}
			}
		};
		loader.schedule();

		/* timeout process */
		BusyIndicator.showWhile(shell.getDisplay(), () -> {
			if (Thread.interrupted())
				return; // loaded before timeout start

			try {
				Thread.sleep(timeout);
				if (Thread.interrupted()) // loaded just after timeout
					return;

				loader.cancel(); // cancel loading job
				dataModelChanged(null);
				logger.error("Loader timeout on model " + current);
				errors.put(current, new ModelLoadingException("Timeout"));
				MessageDialog.openError(shell, "Cannot load model",
						"Unable to load model " + current.getDescription() + ": Timeout.");
			} catch (InterruptedException e) {
				// This is supposed to happen -- do nothing
			}
		});
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
package org.tomvej.fmassoc.parts.model.core;

import javax.inject.Inject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
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
	public void execute(ModelEntry current, Shell shell) {
		events.post(DataModelTopic.MODEL_LOADING, current.getLabel());

		Thread currentThread = Thread.currentThread();
		Job loader = new Job("Loading model " + current) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					DataModel model = current.load();
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
					currentThread.interrupt();
					dataModelChanged(model);
					logger.info("Model loaded: " + current);
					return Status.OK_STATUS;
				} catch (ModelLoadingException mle) {
					if (!monitor.isCanceled()) {
						currentThread.interrupt();
						dataModelChanged(null);
						context.set(ModelEntry.class, null);

						String message = "Unable to load model " + current.getDescription() + ": "
								+ mle.getLocalizedMessage();
						shell.getDisplay().asyncExec(() -> MessageDialog.openError(shell, "Cannot load model", message));
					}
					return new Status(IStatus.ERROR, Constants.PLUGIN_ID, "Unable to load model: " + current, mle);
				}
			}
		};
		loader.schedule();
		BusyIndicator.showWhile(shell.getDisplay(), () -> {
			if (Thread.interrupted()) {
				return;
			}

			try {
				Thread.sleep(5000);
				if (Thread.interrupted()) {
					return;
				}

				loader.cancel();
				dataModelChanged(null);
				logger.error("Loader timeout on model " + current);
				MessageDialog.openError(shell, "Cannot load model",
						"Unable to load model " + current.getDescription() + ": Timeout.");
			} catch (InterruptedException e) {
				// This is supposed to happen
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
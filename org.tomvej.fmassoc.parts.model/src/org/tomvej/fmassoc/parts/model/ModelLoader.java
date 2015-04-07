package org.tomvej.fmassoc.parts.model;

import org.eclipse.jface.wizard.IWizard;
import org.tomvej.fmassoc.model.db.DataModel;

/**
 * Provides model loading functionality (for a certain model type).
 * 
 * @author Tomáš Vejpustek
 */
public interface ModelLoader {
	/**
	 * Load model with given ID.
	 * 
	 * @throws ModelLoadingException
	 *             when the model could not be loaded.
	 */
	DataModel loadModel(String id) throws ModelLoadingException;

	/**
	 * Create "New Model" wizard for given model ID.
	 */
	IWizard createNewWizard(String id);

	/**
	 * Create "Edit Model" wizard for given model ID.
	 */
	IWizard createEditWizard(String id);
}

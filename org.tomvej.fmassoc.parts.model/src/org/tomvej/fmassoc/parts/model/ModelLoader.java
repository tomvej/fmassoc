package org.tomvej.fmassoc.parts.model;

import org.tomvej.fmassoc.model.db.DataModel;

public interface ModelLoader {
	/**
	 * Load model with given ID.
	 * 
	 * @throws ModelLoadingException
	 *             when the model could not be loaded.
	 */
	DataModel loadModel(String id) throws ModelLoadingException;
}

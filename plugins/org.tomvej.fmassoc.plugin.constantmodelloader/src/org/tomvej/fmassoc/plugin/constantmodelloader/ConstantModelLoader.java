package org.tomvej.fmassoc.plugin.constantmodelloader;

import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.parts.model.ModelLoader;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;

/**
 * Creates only one model (fragment of FM data model) independently on model id.
 * Used for testing purposes.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class ConstantModelLoader implements ModelLoader {

	@Override
	public DataModel loadModel(String id) throws ModelLoadingException {
		// TODO Auto-generated method stub
		return null;
	}
}

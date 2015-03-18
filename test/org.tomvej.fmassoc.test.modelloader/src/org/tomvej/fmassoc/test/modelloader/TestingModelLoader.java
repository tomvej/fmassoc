package org.tomvej.fmassoc.test.modelloader;

import org.eclipse.jface.wizard.IWizard;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.parts.model.ModelLoader;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;

public class TestingModelLoader implements ModelLoader {

	@Override
	public DataModel loadModel(String id) throws ModelLoadingException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWizard createEditWizard(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IWizard createNewWizard(String id) {
		// TODO Auto-generated method stub
		return null;
	}
}

package org.tomvej.fmassoc.test.modelloader;

import org.eclipse.jface.wizard.IWizard;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.parts.model.ModelLoader;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;
import org.tomvej.fmassoc.test.modelloader.wizard.TestModelWizard;

public class TestingModelLoader implements ModelLoader {

	@Override
	public DataModel loadModel(String id) throws ModelLoadingException {
		ModelStorage settings = new ModelStorage(id);
		try {
			Thread.sleep(settings.getDuration());
		} catch (InterruptedException e) {
			throw new ModelLoadingException("Interrupted while loading.", e);
		}
		if (settings.fails()) {
			throw new ModelLoadingException("This model must fail.");
		}
		return new EmptyDataModel();
	}

	@Override
	public IWizard createEditWizard(String id) {
		return new TestModelWizard(id);
	}

	@Override
	public IWizard createNewWizard(String id) {
		return new TestModelWizard(id);
	}
}

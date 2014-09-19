package org.tomvej.fmassoc.plugin.mobilemodelloader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.jface.wizard.IWizard;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.parts.model.ModelLoader;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;
import org.tomvej.fmassoc.plugin.mobilemodelloader.xml.DataModelNode;

public class MobileModelLoader implements ModelLoader {

	@Override
	public DataModel loadModel(String id) throws ModelLoadingException {
		try {
			Unmarshaller unmarshaller = JAXBContext.newInstance(DataModelNode.class).createUnmarshaller();
		} catch (JAXBException jaxbe) {
			throw new ModelLoadingException("Unable to load model from " + ".", jaxbe);
		}
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

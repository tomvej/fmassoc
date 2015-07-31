package org.tomvej.fmassoc.plugin.examplemodelloader;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.jface.wizard.IWizard;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.parts.model.ModelLoader;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;
import org.tomvej.fmassoc.plugin.examplemodelloader.xml.DataModelNode;

public class ExampleModelLoader implements ModelLoader {

	@Override
	public IWizard createNewWizard(String id) {
		return null; // empty implementation
	}

	@Override
	public IWizard createEditWizard(String id) {
		return null; // empty implementation
	}

	@Override
	public DataModel loadModel(String id) throws ModelLoadingException {
		throw new ModelLoadingException("Not yet supported");
	}

	private static DataModel loadModel(InputStream is) throws ModelLoadingException, JAXBException {
		Unmarshaller unmarshaller = JAXBContext.newInstance(DataModelNode.class).createUnmarshaller();
		return ((DataModelNode) unmarshaller.unmarshal(is)).transform();
	}


}

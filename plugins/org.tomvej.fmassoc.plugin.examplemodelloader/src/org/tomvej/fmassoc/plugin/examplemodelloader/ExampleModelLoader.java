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
		return new SinglePageWizard(new ExampleWizardPage("New Example Data Model", "Create new example data model."));
	}

	@Override
	public IWizard createEditWizard(String id) {
		return new SinglePageWizard(new ExampleWizardPage("Edit Example Data Model", "Edit new example data model."));
	}

	@Override
	public DataModel loadModel(String id) throws ModelLoadingException {
		try {
			InputStream input = getClass().getClassLoader().getResourceAsStream("fate.xml");
			Unmarshaller unmarshaller = JAXBContext.newInstance(DataModelNode.class).createUnmarshaller();
			return ((DataModelNode) unmarshaller.unmarshal(input)).transform();
		} catch (JAXBException jaxbe) {
			throw new ModelLoadingException("Unable to load model from file.", jaxbe);
		}
	}

}

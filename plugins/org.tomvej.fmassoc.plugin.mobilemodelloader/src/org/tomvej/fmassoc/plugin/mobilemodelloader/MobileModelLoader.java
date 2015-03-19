package org.tomvej.fmassoc.plugin.mobilemodelloader;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.jface.wizard.IWizard;
import org.osgi.service.prefs.BackingStoreException;
import org.tomvej.fmassoc.model.builder.simple.DataModelBuilder;
import org.tomvej.fmassoc.model.builder.simple.TableCache;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.parts.model.ModelLoader;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;
import org.tomvej.fmassoc.plugin.mobilemodelloader.wizards.MobileModelWizard;
import org.tomvej.fmassoc.plugin.mobilemodelloader.xml.DataModelNode;

/**
 * Model loader for the field manager data model.
 * 
 * @author Tomáš Vejpustek
 */
public class MobileModelLoader implements ModelLoader {

	@Override
	public DataModel loadModel(String id) throws ModelLoadingException {
		try {
			ModelStorage pref = new ModelStorage(id);
			Pair<DataModelBuilder, TableCache<String>> result = loadModelBuilder(new File(pref.getFile()));
			DataModelBuilder builder = result.getLeft();
			TableCache<String> byName = result.getRight();

			pref.getForbidden().stream().map(n -> byName.get(n)).filter(t -> t != null)
					.forEach(t -> builder.addForbidden(t));

			return builder.create();
		} catch (JAXBException jaxbe) {
			throw new ModelLoadingException(jaxbe);
		} catch (BackingStoreException bse) {
			throw new ModelLoadingException("Cannot access stored model preferences.", bse);
		}
	}

	@Override
	public IWizard createEditWizard(String id) {
		return new MobileModelWizard(id, true);
	}

	@Override
	public IWizard createNewWizard(String id) {
		return new MobileModelWizard(id, false);
	}

	private static Pair<DataModelBuilder, TableCache<String>> loadModelBuilder(File target) throws ModelLoadingException,
			JAXBException {
		Unmarshaller unmarshaller = JAXBContext.newInstance(DataModelNode.class).createUnmarshaller();
		DataModelNode model = (DataModelNode) unmarshaller.unmarshal(target);
		return model.transform();
	}

	/**
	 * Attempts to load data model from target file.
	 */
	public static DataModel loadModel(File target) throws ModelLoadingException, JAXBException {
		return loadModelBuilder(target).getLeft().create();
	}
}

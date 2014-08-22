package org.tomvej.fmassoc.parts.model.core;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.wizard.IWizard;
import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;

/**
 * Contains information necessary for data model loading and display.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class ModelEntry {
	private final ModelLoaderEntry loader;
	private final String id;
	private final String label;

	ModelEntry(String id, String label, ModelLoaderEntry loader) {
		this.id = Validate.notBlank(id);
		this.label = label;
		this.loader = Validate.notNull(loader);
	}

	/**
	 * Return model loader used to load this model.
	 */
	public ModelLoaderEntry getLoader() {
		return loader;
	}

	/**
	 * Return unique data model id (generated internally).
	 */
	public String getId() {
		return id;
	}

	/**
	 * Return descriptive label.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Create new wizard for this model from the associated loader.
	 */
	public IWizard createNewWizard() {
		return getLoader().getLoader().createNewWizard(getId());
	}

	/**
	 * Create edit wizard for this model from the associated loader.
	 */
	public IWizard createEditWizard() {
		return getLoader().getLoader().createEditWizard(getId());
	}

	/**
	 * Load the model with the associated loader.
	 * 
	 * @return Loaded model.
	 * @throws ModelLoadingException
	 *             when the loading was not successful.
	 */
	public DataModel load() throws ModelLoadingException {
		return getLoader().getLoader().loadModel(getId());
	}

	/**
	 * Return string describing this model. To be used mostly in error dialogs.
	 */
	public String getDescription() {
		return getLabel() + " (" + getLoader().getName() + ")";
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ModelEntry)) {
			return false;
		}
		return ((ModelEntry) obj).getId().equals(getId());
	}

	@Override
	public String toString() {
		return "Model " + getLabel() + " [" + getId() + ", " + getLoader().toString() + "]";
	}
}

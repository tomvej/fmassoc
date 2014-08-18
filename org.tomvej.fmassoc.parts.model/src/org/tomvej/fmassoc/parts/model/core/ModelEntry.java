package org.tomvej.fmassoc.parts.model.core;

import org.apache.commons.lang3.Validate;

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
}

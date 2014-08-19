package org.tomvej.fmassoc.parts.model.core;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.parts.model.ModelLoader;

/**
 * Describes model loader.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class ModelLoaderEntry {
	private final String name, description;
	private final ModelLoader loader;

	ModelLoaderEntry(String name, String description, ModelLoader loader) {
		this.name = name;
		this.description = description;
		this.loader = Validate.notNull(loader);
	}

	/**
	 * Return model loader name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return longer description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Return the actual model loader.
	 */
	public ModelLoader getLoader() {
		return loader;
	}

	@Override
	public int hashCode() {
		return loader.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ModelLoaderEntry)) {
			return false;
		}
		return ((ModelLoaderEntry) obj).getLoader().equals(getLoader());
	}

	@Override
	public String toString() {
		return "ModelLoader " + getName() + " [" + getLoader().getClass().getCanonicalName() + "]";
	}

}

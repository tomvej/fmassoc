package org.tomvej.fmassoc.core.extension;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.IConfigurationElement;

/**
 * Wraps most commonly used attributes of extensions -- name and description.
 * 
 * @author Tomáš Vejpustek
 */
public class ExtensionEntry {
	private final IConfigurationElement config;

	/**
	 * Create from configuration element.
	 */
	public ExtensionEntry(IConfigurationElement configuration) {
		config = Validate.notNull(configuration);
	}

	protected IConfigurationElement getConfiguration() {
		return config;
	}

	/**
	 * Return name (short description).
	 */
	public String getName() {
		return config.getAttribute("name");
	}

	/**
	 * Return long description.
	 */
	public String getDescription() {
		return config.getAttribute("description");
	}

	/**
	 * Return id of this extension, more precisely the id of the class it
	 * provides.
	 */
	public String getId() {
		return config.getAttribute("class");
	}
}

package org.tomvej.fmassoc.parts.paths;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * Wrapper of label provider extension.
 * 
 * @author Tomáš Vejpustek
 */
public class LabelProviderEntry {
	private final IConfigurationElement config;

	/**
	 * Create from configuration element.
	 */
	public LabelProviderEntry(IConfigurationElement configuration) {
		this.config = Validate.notNull(configuration);
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
	 * Return id (used internally).
	 */
	public String getId() {
		return config.getAttribute("class");
	}

	/**
	 * Create an instance of this label provider.
	 */
	public ColumnLabelProvider create() throws CoreException {
		return (ColumnLabelProvider) config.createExecutableExtension("class");
	}


}

package org.tomvej.fmassoc.parts.paths.preference;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.tomvej.fmassoc.core.extension.ExtensionEntry;

/**
 * Wrapper of label provider extension.
 * 
 * @author Tomáš Vejpustek
 */
public class LabelProviderEntry extends ExtensionEntry {

	/**
	 * Create from configuration element.
	 */
	public LabelProviderEntry(IConfigurationElement configuration) {
		super(configuration);
	}

	/**
	 * Create an instance of this label provider.
	 */
	public ColumnLabelProvider create() throws CoreException {
		return (ColumnLabelProvider) getConfiguration().createExecutableExtension("class");
	}

}

package org.tomvej.fmassoc.core.properties;

import java.util.Comparator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.tomvej.fmassoc.core.extension.ExtensionEntry;
import org.tomvej.fmassoc.model.property.PathProperty;

/**
 * Wrapper of path property extension.
 * 
 * @author Tomáš Vejpustek
 * @param <T>
 *            Type of path property values.
 */
public class PathPropertyEntry<T> extends ExtensionEntry {
	private final PathProperty<T> property;
	private final Comparator<T> comparator;

	/**
	 * Create from configuraion element.
	 * 
	 * @throws CoreException
	 *             when it could not be created.
	 */
	@SuppressWarnings("unchecked")
	public PathPropertyEntry(IConfigurationElement config) throws CoreException {
		super(config);
		property = (PathProperty<T>) config.createExecutableExtension("class");
		if (config.getAttribute("comparator") != null) {
			comparator = (Comparator<T>) config.createExecutableExtension("comparator");
		} else {
			comparator = null;
		}
	}

	/**
	 * Returns this path property.
	 */
	public PathProperty<T> getProperty() {
		return property;
	}

	/**
	 * Return comparator for path property values. Can be {@code null}.
	 */
	public Comparator<T> getComparator() {
		return comparator;
	}

}

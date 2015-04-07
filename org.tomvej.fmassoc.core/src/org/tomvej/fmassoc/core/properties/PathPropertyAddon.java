package org.tomvej.fmassoc.core.properties;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;
import org.tomvej.fmassoc.core.communicate.ContextObjects;

class PathPropertyAddon {

	@PostConstruct
	public void load(IExtensionRegistry registry, IEclipseContext context, Logger logger) {
		List<PathPropertyEntry<?>> properties = new ArrayList<>();
		for (IConfigurationElement elem : registry.getConfigurationElementsFor("org.tomvej.fmassoc.core.pathProperty")) {
			try {
				properties.add(new PathPropertyEntry<>(elem));
			} catch (CoreException ce) {
				logger.warn(ce, "Cannot load path property " + elem.getAttribute("class"));
			}
		}
		logger.info("Path properties loaded.");
		context.set(ContextObjects.PATH_PROPERTIES, properties);
	}
}
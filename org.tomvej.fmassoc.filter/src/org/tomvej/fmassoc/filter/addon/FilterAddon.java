package org.tomvej.fmassoc.filter.addon;

import javax.annotation.PostConstruct;

import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.log.Logger;
import org.tomvej.fmassoc.core.extension.ReferenceExtensionRegistry;
import org.tomvej.fmassoc.filter.FilterProvider;
import org.tomvej.fmassoc.filter.FilterRegistry;

/**
 * Loads filter providers and puts them into context.
 * 
 * @author Tomáš Vejpustek
 */
public class FilterAddon {

	/**
	 * Load filter providers.
	 */
	@PostConstruct
	public void loadFilters(IExtensionRegistry extensions, Logger logger, IEclipseContext context) {
		context.set(FilterRegistry.class, new Registry(new ReferenceExtensionRegistry<>(
				extensions.getConfigurationElementsFor("org.tomvej.fmassoc.filter.filterprovider"),
				FilterProvider.class, logger)));
		logger.info("Filters loaded.");
	}

	private static class Registry implements FilterRegistry {
		@SuppressWarnings("rawtypes")
		private final ReferenceExtensionRegistry<FilterProvider> providers;

		@SuppressWarnings("rawtypes")
		private Registry(ReferenceExtensionRegistry<FilterProvider> providers) {
			this.providers = providers;
		}

		@Override
		public FilterProvider<?> apply(Class<?> arg0) {
			return providers.apply(arg0);
		}
	}
}

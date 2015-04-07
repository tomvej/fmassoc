package org.tomvej.fmassoc.core.extension;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.e4.core.services.log.Logger;

/**
 * Registry for same-type extensions for certain classes. Only the newest
 * extension for given class is used. Extensions have to contain:
 * <ul>
 * <li>{@code class} -- object provided by extension,</li>
 * <li>{@code date} -- date in yyyy-MM-dd format,</li>
 * <li>{@code reference} -- class for which the extension is.</li>
 * </ul>
 * 
 * @author Tomáš Vejpustek
 */
public class ReferenceExtensionRegistry<T> implements Function<Class<?>, T> {
	private static final DateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

	private final Map<Class<?>, T> reference;

	/**
	 * Load extensions.
	 * 
	 * @param elements
	 *            Configuration elements.
	 * @param clazz
	 *            Class of extension.
	 * @param logger
	 *            Logger which will log errors and warning.
	 */
	public ReferenceExtensionRegistry(IConfigurationElement[] elements, Class<T> clazz, Logger logger) {
		Map<Class<?>, Pair<T, Date>> references = new HashMap<>();
		for (IConfigurationElement config : elements) {
			try {
				Class<?> refClass = Class.forName(config.getAttribute("reference"));
				Date date = getDate(config, logger);

				// get existing extension
				Pair<T, Date> existing = references.get(refClass);
				if (existing == null || (date != null
						&& (existing.getRight() == null || date.after(existing.getRight())))) {

					// replace element
					Object result = config.createExecutableExtension("class");
					if (clazz.isInstance(result)) {
						references.put(refClass, Pair.of(clazz.cast(result), date));
					} else {
						logger.error("Cannot cast extension ({}) to {} for {}.",
								new Object[] { result.getClass().getName(), clazz.getName(), formatExtension(config) });
					}
				}
			} catch (ClassNotFoundException cnfe) {
				logger.error("Cannot find referenced class `{}' for {}.",
						config.getAttribute("refernce"), formatExtension(config));
			} catch (CoreException ce) {
				logger.error(ce, "Cannot create extension for " + formatExtension(config) + ".");
			}
		}

		reference = references.entrySet().stream().collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getLeft()));
	}

	private static String formatExtension(IConfigurationElement elem) throws InvalidRegistryObjectException {
		return elem.getContributor().getName() + "." + elem.getName();
	}

	private static Date getDate(IConfigurationElement config, Logger logger) {
		try {
			return FORMAT.parse(config.getAttribute("date"));
		} catch (ParseException e) {
			logger.warn("Cannot parse date `{}' for {}. Using null.",
					config.getAttribute("date"), formatExtension(config));
			return null;
		}
	}

	@Override
	public T apply(Class<?> t) {
		return reference.get(t);
	}
}

package org.tomvej.fmassoc.test.path.aggregator;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.di.extensions.EventTopic;
import org.eclipse.e4.core.services.log.Logger;
import org.tomvej.fmassoc.core.communicate.ContextObjects;
import org.tomvej.fmassoc.core.communicate.PathSearchTopic;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Writes aggregate path property values for found paths into log. Min and max
 * are logged for comparables, all distinct values are logged for Boolean, Enums
 * and non-comparables.
 * 
 * @author Tomáš Vejpustek
 */
public class PathAggregator {
	@Inject
	private Logger logger;
	@Inject
	@Named(ContextObjects.PATH_PROPERTIES)
	private List<PathPropertyEntry<?>> properties;

	/**
	 * Listens to path search finish.
	 */
	@Inject
	@Optional
	public void searchFinished(@EventTopic(PathSearchTopic.FINISH) Object result,
			@Named(ContextObjects.FOUND_PATHS) List<Path> found) {
		logAggregate(found);
	}

	/**
	 * Listens to path search cancel.
	 */
	@Inject
	@Optional
	public void searchCancelled(@EventTopic(PathSearchTopic.CANCEL) Object result,
			@Named(ContextObjects.FOUND_PATHS) List<Path> found) {
		logAggregate(found);
	}

	private void logAggregate(List<Path> found) {
		if (found.isEmpty()) {
			return;
		}
		StringBuilder log = new StringBuilder("Found paths aggregate property values:");
		properties.forEach(p -> log.append("\n" + p.getName() + " " + createAggregate(found, p)));
		logger.info(log.toString());
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> String createAggregate(List<Path> path, PathPropertyEntry<T> property) {
		Stream<T> propertyValues = path.stream().map(p -> property.getProperty().getValue(p));

		if (Boolean.class.equals(property.getProperty().getType()) || property.getProperty().getType().isEnum()) {
			return createValuesAggregate(propertyValues);
		}
		if (property.getComparator() != null) {
			return createMinMaxAggregate(propertyValues, property.getComparator());
		}
		if (Comparable.class.isAssignableFrom(property.getProperty().getType())) {
			return createMinMaxAggregate(propertyValues, new ComparableComparator());
		}
		return createValuesAggregate(propertyValues);
	}

	private <T> String createValuesAggregate(Stream<T> values) {
		Set<T> result = values.collect(Collectors.toSet());
		if (result.size() == 1) {
			return "= " + result.iterator().next();
		} else {
			return "in " + result;
		}
	}

	private <T> String createMinMaxAggregate(Stream<T> values, Comparator<? super T> comparator) {
		Collection<T> valueCollection = values.collect(Collectors.toList());
		T min = valueCollection.stream().min(comparator).get();
		T max = valueCollection.stream().max(comparator).get();
		if (min.equals(max)) {
			return "= " + min;
		} else {
			return "in [" + min + " -> " + max + "]";
		}
	}
}

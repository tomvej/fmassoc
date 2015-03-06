package org.tomvej.fmassoc.test.path.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.model.path.Path;

public class SinglePathChecker {
	private final Path path;
	private final SearchInput input;
	private final List<String> errors = new ArrayList<>();

	SinglePathChecker(Path path, SearchInput input) {
		this.path = path;
		this.input = input;

		append(checkSource());
		append(checkDestination());
		append(checkMissingInterrmitent());
		append(checkIntermittentOrder());
	}

	public List<String> getErrors() {
		return Collections.unmodifiableList(errors);
	}

	private void append(String error) {
		if (error != null) {
			errors.add(error);
		}
	}


	private String checkSource() {
		if (!path.getSource().equals(input.getSource())) {
			return "Incorrect source: expected " + input.getSource().getName() + ", got " + path.getSource().getName();
		}
		return null;
	}

	private String checkDestination() {
		Table expected = input.getDestinations().get(input.getDestinations().size() - 1);
		if (!path.getDestination().equals(expected)) {
			return "Incorrect destination: expected " + expected.getName() + ", got " + path.getDestination().getName();
		}
		return null;
	}

	private String checkMissingInterrmitent() {
		Set<Table> pathTables = getPathIntermittent().collect(Collectors.toSet());
		List<Table> missing = getInputIntermittent().filter(t -> !pathTables.contains(t)).collect(Collectors.toList());
		if (missing.isEmpty()) {
			return null;
		}

		StringBuilder result = new StringBuilder("Missing intermmitent tables: ");
		missing.forEach(t -> result.append(t.getName()).append(", "));
		result.delete(result.length() - 2, result.length());
		return result.toString();
	}

	private String checkIntermittentOrder() {
		Set<Table> inputInterSet = getInputIntermittent().collect(Collectors.toSet());
		List<Table> pathInter = getPathIntermittent().filter(t -> inputInterSet.contains(t)).collect(Collectors.toList());
		Set<Table> pathInterSet = new HashSet<>(pathInter);
		List<Table> inputInter = getInputIntermittent().filter(t -> pathInterSet.contains(t)).collect(Collectors.toList());
		if (!inputInter.equals(pathInter)) {
			return "Wrong intermittent tables order.";
		}
		return null;
	}

	private Stream<Table> getPathIntermittent() {
		return path.getAssociations().stream().map(a -> a.getSource()).skip(1);
	}

	private Stream<Table> getInputIntermittent() {
		return input.getDestinations().stream().limit(input.getDestinations().size() - 1);
	}
}
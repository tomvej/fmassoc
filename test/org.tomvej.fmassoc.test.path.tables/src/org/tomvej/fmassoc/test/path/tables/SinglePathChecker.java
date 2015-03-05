package org.tomvej.fmassoc.test.path.tables;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

}
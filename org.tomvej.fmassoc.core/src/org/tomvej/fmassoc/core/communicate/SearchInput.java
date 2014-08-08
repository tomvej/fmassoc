package org.tomvej.fmassoc.core.communicate;

import java.util.List;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Input for the search algorithm.
 * 
 * @author Tomáš Vejpustek
 */
public class SearchInput {
	private final Table source;
	private final List<Table> destinations;

	/**
	 * Specify source table and destination tables.
	 */
	public SearchInput(Table source, List<Table> destinations) {
		this.source = Validate.notNull(source);
		this.destinations = Validate.notEmpty(destinations);
	}

	/**
	 * Retrieve source table.
	 */
	public Table getSource() {
		return source;
	}

	/**
	 * Retrieve destination tables.
	 */
	public List<Table> getDestinations() {
		return destinations;
	}

}

package org.tomvej.fmassoc.core.search;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		this.destinations = Validate.notEmpty(destinations, "There has to be at least one destination.");
		Set<Table> inter = new HashSet<>(destinations);
		Validate.isTrue(!inter.contains(null), "Destination tables contain null elements.");
		Validate.isTrue(destinations.size() == inter.size(), "Destination tables contain duplicates.");
		Validate.isTrue(!inter.contains(source), "Destination tables contain source table.");
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

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof SearchInput)) {
			return false;
		}
		SearchInput other = (SearchInput) obj;
		return other.getSource().equals(getSource()) && other.getDestinations().equals(other.getDestinations());
	}

	@Override
	public int hashCode() {
		return getDestinations().hashCode() + 59 * getSource().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Search Input [").append(getSource().getName());
		for (Table destination : getDestinations()) {
			result.append(" -> ").append(destination.getName());
		}
		return result.append("]").toString();
	}

}

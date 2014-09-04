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
	private final Set<Table> forbidden;

	/**
	 * Specify source table, destination tables and forbidden tables.
	 */
	public SearchInput(Table source, List<Table> destinations, Set<Table> forbidden) {
		this.source = Validate.notNull(source);
		this.destinations = Validate.notEmpty(destinations, "There has to be at least one destination.");
		this.forbidden = Validate.notNull(forbidden);
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

	/**
	 * Retrieve forbidden tables.
	 */
	public Set<Table> getForbidden() {
		return forbidden;
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
		return other.getSource().equals(getSource()) && other.getDestinations().equals(getDestinations()) &&
				other.getForbidden().equals(getForbidden());
	}

	@Override
	public int hashCode() {
		return 59 * (getDestinations().hashCode() + 59 * getSource().hashCode()) + getForbidden().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("Search Input [").append(getSource().getName());
		for (Table destination : getDestinations()) {
			result.append(" -> ").append(destination.getName());
		}
		if (!getForbidden().isEmpty()) {
			result.append(" x ");
			for (Table forbidden : getForbidden()) {
				result.append(forbidden.getName()).append(", ");
			}
			int length = result.length();
			result.delete(length - 2, length);
		}
		return result.append("]").toString();
	}
}

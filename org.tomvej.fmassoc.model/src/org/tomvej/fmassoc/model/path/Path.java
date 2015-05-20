package org.tomvej.fmassoc.model.path;

import java.util.List;
import java.util.stream.Collectors;

import org.tomvej.fmassoc.model.db.Association;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Path comprised of linked atomic associations. May carry some properties.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public interface Path extends Association, PathInfo {
	/**
	 * List of comprising associations (from source to destination).
	 */
	List<AssociationProperty> getAssociations();

	/**
	 * List of comprising tables (from source to destination).
	 */
	default List<Table> getTables() {
		List<Table> result = getAssociations().stream().
				map(a -> a.getSource()).collect(Collectors.toList());
		result.add(getDestination());
		return result;
	}
}
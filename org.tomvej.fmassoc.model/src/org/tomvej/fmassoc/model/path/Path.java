package org.tomvej.fmassoc.model.path;

import java.util.List;

import org.tomvej.fmassoc.model.db.Association;
import org.tomvej.fmassoc.model.db.AssociationProperty;

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
}
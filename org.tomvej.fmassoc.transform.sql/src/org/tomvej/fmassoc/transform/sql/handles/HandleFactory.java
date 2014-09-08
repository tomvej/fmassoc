package org.tomvej.fmassoc.transform.sql.handles;

import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Core class of formatter settings. Generates handles for database objects.
 * 
 * @author Tomáš Vejpustek
 *
 */
public interface HandleFactory {

	/**
	 * Return a handle for given table.
	 */
	TableHandle getTableHandle(Table table);

	/**
	 * Return a handle for given property (association or otherwise).
	 */
	ColumnHandle getPropertyHandle(Property property);

	/**
	 * Return a handle for an ID property of given table.
	 */
	ColumnHandle getIDPropertyHandle(Table table);
}

package org.tomvej.fmassoc.model.db;

import java.util.Collection;

/**
 * Data model of database.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public interface DataModel {
	/**
	 * Get all tables comprising this data model.
	 * 
	 * @return Unmodifiable collection of tables.
	 */
	Collection<Table> getTables();

	/**
	 * Get tables which are forbidden by default.
	 * 
	 * @return Unmodifiable collection of tables.
	 */
	Collection<Table> getForbiddenTables();
}

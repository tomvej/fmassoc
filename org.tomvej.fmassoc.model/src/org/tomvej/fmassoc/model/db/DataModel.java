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
	 * Table of given name.
	 * 
	 * @return Table of given name, {@code null} when there is none.
	 */
	Table getTableByName(String name);

	/**
	 * Table of given implementation name.
	 * 
	 * @return Table of given implementation name, {@code null} when there is
	 *         none.
	 */
	Table getTableByImplName(String name);

	/**
	 * Table of given number.
	 * 
	 * @return Table of given number, {@code null} when there is none.
	 */
	Table getTableByNumber(int number);

	/**
	 * Names of all tables.
	 * 
	 * @return Unmodifiable collection of names.
	 */
	Collection<String> getTableNames();

	/**
	 * Implementation names of all tables.
	 * 
	 * @return Unmodifiable collection of names.
	 */
	Collection<String> getTableImplNames();
}

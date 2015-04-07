package org.tomvej.fmassoc.model.db;

/**
 * Element of data model which is a property/column of a table.
 * 
 * @author Tomáš Vejpustek
 *
 */
public interface Property extends Named {

	/**
	 * Returns reference to table which contains this property.
	 */
	Table getParent();

}

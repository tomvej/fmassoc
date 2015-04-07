package org.tomvej.fmassoc.model.db;

/**
 * Association between two tables.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public interface Association {
	/**
	 * Table which this association leads from.
	 * 
	 * @return Source table.
	 */
	Table getSource();

	/**
	 * Table which this association leads to.
	 * 
	 * @return Destination table.
	 */
	Table getDestination();
}

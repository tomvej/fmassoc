package org.tomvej.fmassoc.model.db;

/**
 * Element of data model. Has human-readable and implementation name.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public interface Named {
	// note -- description may be added later
	/**
	 * Human-readable name.
	 */
	String getName();

	/**
	 * Implementation name.
	 */
	String getImplName();
}

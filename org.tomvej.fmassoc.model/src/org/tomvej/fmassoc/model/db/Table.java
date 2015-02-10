package org.tomvej.fmassoc.model.db;

import java.util.Collection;

/**
 * Table (type) of data model.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public interface Table extends Named {

	/**
	 * Table number ({@code TFT###...}).
	 * 
	 * Specific to field manager.
	 */
	int getNumber();

	/**
	 * All associations with this table as source or destination.
	 */
	Collection<AssociationProperty> getAssociations();

	/**
	 * Properties other than associations and {@code ID_OBJECT}.
	 */
	Collection<Property> getProperties();

	/**
	 * Returns implementation name (see {@link Named}) of id property.
	 */
	String getIDImplName();
}

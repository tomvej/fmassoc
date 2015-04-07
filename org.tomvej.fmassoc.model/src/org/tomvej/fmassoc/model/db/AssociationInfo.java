package org.tomvej.fmassoc.model.db;

/**
 * Information about association.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public interface AssociationInfo {

	/**
	 * Get association multiplicity.
	 * 
	 * @return Association multiplicity.
	 */
	Multiplicity getMultiplicity();

	/**
	 * Check whether this association is mandatory.
	 * 
	 * @return {@code true} when mandatory, {@code false} otherwise.
	 */
	boolean isMandatory();
}

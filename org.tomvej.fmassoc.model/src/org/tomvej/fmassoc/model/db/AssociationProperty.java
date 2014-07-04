package org.tomvej.fmassoc.model.db;

/**
 * Association which is a property (column) of table. It is an atomic
 * association.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public interface AssociationProperty extends Association, AssociationInfo,
		Property {

	/**
	 * Checks whether the actual association in data model corresponds to this
	 * association source and destination.
	 * 
	 * @return {@code false} if {@link #getSource()} has column corresponding to
	 *         this association, {@code true} if it is {@link #getDestination()}
	 *         .
	 */
	boolean isReverse();

	/**
	 * Return table to whose id this association property references.
	 */
	default Table getOther() {
		return isReverse() ? getSource() : getDestination();
	}

}

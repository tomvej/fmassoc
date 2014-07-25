package org.tomvej.fmassoc.finder.property;

import org.tomvej.fmassoc.model.db.AssociationInfo;
import org.tomvej.fmassoc.model.db.Multiplicity;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.model.property.PathPropertyBuilder;
import org.tomvej.fmassoc.model.property.StackPathProperty;
import org.tomvej.fmassoc.model.property.StackPathPropertyBuilder;

/**
 * Aggregate multiplicity of a path.
 * 
 * @author Tomáš Vejpustek
 *
 */
public enum PathMultiplicity implements PathProperty<Multiplicity> {
	/** Singleton instance */
	INSTANCE;

	private static enum StackProperty implements StackPathProperty<Multiplicity, Multiplicity> {
		INSTANCE;

		@Override
		public Multiplicity getNewValue(Multiplicity previousValue, AssociationInfo target) {
			return previousValue.join(target.getMultiplicity());
		}

		@Override
		public Multiplicity getValue(Multiplicity value) {
			return value;
		}
	}

	public PathPropertyBuilder<Multiplicity> getBuilder() {
		return new StackPathPropertyBuilder<Multiplicity, Multiplicity>(StackProperty.INSTANCE, Multiplicity.ONE_TO_ONE);
	};

	/**
	 * Returns instance of this property;
	 */
	public static PathProperty<Multiplicity> getInstance() {
		return INSTANCE;
	}
}

package org.tomvej.fmassoc.finder.property;

import org.tomvej.fmassoc.model.db.AssociationInfo;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.model.property.PathPropertyBuilder;

/**
 * Path optionality; {@code false} when the path is mandatory, {@code tue} when
 * it is optional.
 * 
 * @author Tomáš Vejpustek
 *
 */
public enum PathOptionality implements PathProperty<Boolean> {
	/** Singleton instance */
	INSTANCE;

	private static class Builder implements PathPropertyBuilder<Boolean> {
		private int optAssociations = 0;

		@Override
		public Boolean getValue() {
			return optAssociations > 0;
		}

		@Override
		public void push(AssociationInfo association) {
			if (!association.isMandatory()) {
				optAssociations++;
			}
		}

		@Override
		public void pop(AssociationInfo association) {
			if (!association.isMandatory()) {
				optAssociations--;
			}
		}
	}

	@Override
	public PathPropertyBuilder<Boolean> getBuilder() {
		return new Builder();
	}

	/**
	 * Return an instance of this class.
	 */
	public static PathProperty<Boolean> getInstance() {
		return INSTANCE;
	}
}

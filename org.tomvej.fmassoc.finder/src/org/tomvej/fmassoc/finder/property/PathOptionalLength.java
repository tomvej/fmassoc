package org.tomvej.fmassoc.finder.property;

import org.tomvej.fmassoc.model.db.AssociationInfo;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.model.property.PathPropertyBuilder;

/**
 * Number of optional associations comprising a path.
 * 
 * @author Tomáš Vejpustek
 *
 */
public enum PathOptionalLength implements PathProperty<Integer> {
	/** Singleton instance */
	INSTANCE;

	private static class Builder implements PathPropertyBuilder<Integer> {
		private int length;

		@Override
		public void push(AssociationInfo association) {
			if (!association.isMandatory()) {
				length++;
			}
		}

		@Override
		public void pop(AssociationInfo association) {
			if (!association.isMandatory()) {
				length--;
			}
		}

		@Override
		public Integer getValue() {
			return length;
		}
	}

	@Override
	public PathPropertyBuilder<Integer> getBuilder() {
		return new Builder();
	}

	/**
	 * Return an instance of this property.
	 */
	public static PathProperty<Integer> getInstance() {
		return INSTANCE;
	}
}

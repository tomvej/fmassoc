package org.tomvej.fmassoc.plugin.properties.basic;

import org.tomvej.fmassoc.model.db.AssociationInfo;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.model.property.PathPropertyBuilder;

/**
 * Number of optional associations comprising a path.
 * 
 * @author Tomáš Vejpustek
 *
 */
public final class PathOptionalLength implements PathProperty<Integer> {

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

	@Override
	public int hashCode() {
		return 5059;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof PathOptionalLength;
	}

}

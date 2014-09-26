package org.tomvej.fmassoc.plugin.properties.basic;

import org.tomvej.fmassoc.model.db.AssociationInfo;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.model.property.PathPropertyBuilder;

/**
 * Path optionality; {@code true} when the path is mandatory, {@code false} when
 * it is optional.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class PathOptionality implements PathProperty<Boolean> {

	private static class Builder implements PathPropertyBuilder<Boolean> {
		private int optAssociations = 0;

		@Override
		public Boolean getValue() {
			return optAssociations == 0;
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

}
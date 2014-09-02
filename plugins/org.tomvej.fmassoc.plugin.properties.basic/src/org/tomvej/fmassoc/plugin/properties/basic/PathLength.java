package org.tomvej.fmassoc.plugin.properties.basic;

import org.tomvej.fmassoc.model.db.AssociationInfo;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.model.property.PathPropertyBuilder;

/**
 * Length of path. This is an auxiliary class for length to conform to
 * {@link PathProperty} interface. Otherwise, {@link Path#getLength()} can be
 * used.
 * 
 * @author Tomáš Vejpustek
 */
public class PathLength implements PathProperty<Integer> {

	private static class Builder implements PathPropertyBuilder<Integer> {
		private int length = 0;

		@Override
		public Integer getValue() {
			return length;
		}

		@Override
		public void pop(AssociationInfo association) {
			length--;
		}

		@Override
		public void push(AssociationInfo association) {
			length++;
		}
	}

	@Override
	public PathPropertyBuilder<Integer> getBuilder() {
		return new Builder();
	}

	@Override
	public Integer getValue(Path target) {
		return target.getLength();
	}
}

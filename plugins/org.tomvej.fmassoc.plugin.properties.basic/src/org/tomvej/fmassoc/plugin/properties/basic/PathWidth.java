package org.tomvej.fmassoc.plugin.properties.basic;

import org.apache.commons.lang3.tuple.Pair;
import org.tomvej.fmassoc.model.db.AssociationInfo;
import org.tomvej.fmassoc.model.db.Multiplicity;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.model.property.PathPropertyBuilder;
import org.tomvej.fmassoc.model.property.StackPathProperty;
import org.tomvej.fmassoc.model.property.StackPathPropertyBuilder;

/**
 * Number of tables on path where multiplicity switches from 1:N to N:1. Since
 * atomic paths cannot be M:N, the only
 * way a M:N path can occur is by joining 1:N and N:1 subpaths. Number of tables
 * where this occur is path width.
 * 
 * <p>
 * Such tables usually comprise "central" types in the model, to which many
 * different objects are attached. The user will usually want to find paths
 * going through at most one of these "central" types.
 * </p>
 * 
 * @author Tomáš Vejpustek
 *
 */
public final class PathWidth implements PathProperty<Integer> {

	private static enum StackProperty implements StackPathProperty<Integer, Pair<Multiplicity, Integer>> {
		INSTANCE;

		@Override
		public Pair<Multiplicity, Integer> getNewValue(Pair<Multiplicity, Integer> previousValue, AssociationInfo target) {
			Multiplicity joint = previousValue.getLeft().join(target.getMultiplicity());
			if (Multiplicity.MANY_TO_MANY.equals(joint)) {
				return Pair.of(target.getMultiplicity(), previousValue.getRight() + 1);
			} else {
				return Pair.of(joint, previousValue.getRight());
			}
		}

		@Override
		public Integer getValue(Pair<Multiplicity, Integer> value) {
			return value.getRight();
		}
	}

	@Override
	public PathPropertyBuilder<Integer> getBuilder() {
		return new StackPathPropertyBuilder<Integer, Pair<Multiplicity, Integer>>(
				StackProperty.INSTANCE, Pair.of(Multiplicity.ONE_TO_ONE, 0));
	}

	@Override
	public Class<Integer> getType() {
		return Integer.class;
	}

	@Override
	public int hashCode() {
		return 3539;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof PathWidth;
	}
}

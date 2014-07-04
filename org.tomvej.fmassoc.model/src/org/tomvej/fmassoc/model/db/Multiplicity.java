package org.tomvej.fmassoc.model.db;

/**
 * Multiplicity of association.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public enum Multiplicity {
	/** One to one multiplicity. */
	ONE_TO_ONE() {
		@Override
		public Multiplicity join(Multiplicity target) {
			return target;
		}
	},
	/** One to many multiplicity. */
	ONE_TO_MANY() {
		@Override
		public Multiplicity inverse() {
			return MANY_TO_ONE;
		}

		@Override
		public Multiplicity join(Multiplicity target) {
			switch (target) {
				case ONE_TO_ONE:
				case ONE_TO_MANY:
					return ONE_TO_MANY;
				case MANY_TO_ONE:
				case MANY_TO_MANY:
					return MANY_TO_MANY;
				default:
					throw unknownMultiplicity(target);
			}
		}
	},
	/** Many to one multiplicity. */
	MANY_TO_ONE() {
		@Override
		public Multiplicity inverse() {
			return ONE_TO_MANY;
		}

		@Override
		public Multiplicity join(Multiplicity target) {
			switch (target) {
				case ONE_TO_ONE:
				case MANY_TO_ONE:
					return MANY_TO_ONE;
				case ONE_TO_MANY:
				case MANY_TO_MANY:
					return MANY_TO_MANY;
				default:
					throw unknownMultiplicity(target);
			}
		}
	},
	/** Many to many multiplicity. */
	MANY_TO_MANY() {
		@Override
		public Multiplicity join(Multiplicity target) {
			return MANY_TO_MANY;
		}
	};

	/**
	 * Inverse multiplicity (i.e. {@link #ONE_TO_MANY} for {@link #MANY_TO_ONE}
	 * ). Double inverse returns the original multiplicity.
	 */
	public Multiplicity inverse() {
		return this;
	}

	/**
	 * Return multiplicity of two joined associations with given multiplicity.
	 * This operation is associative.
	 * 
	 * @param target
	 *            Multiplicity of right-joined association.
	 */
	public abstract Multiplicity join(Multiplicity target);

	/**
	 * Return multiplicity of two joined association with given multiplicity.
	 * This operation is associative.
	 * 
	 * @param first
	 *            Multiplicity of left-joined association.
	 * @param second
	 *            Multiplicity of right-joined association.
	 */
	public static Multiplicity join(Multiplicity first, Multiplicity second) {
		return first.join(second);
	}

	/**
	 * Error to be used in {@code default} statement of {@code switch}
	 * statements.
	 */
	protected IllegalArgumentException unknownMultiplicity(Multiplicity target) {
		return new IllegalArgumentException("Unknown multiplicity: " + target);
	}

}

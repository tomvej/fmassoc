package org.tomvej.fmassoc.filter.operator;


/**
 * Operators applicable on comparables.
 * 
 * TODO This could use a refactoring since it uses raw types.
 * 
 * @author Tomáš Vejpustek
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public enum ComparableOperator implements Operator<Comparable> {
	/** Greater than operator */
	GT(">") {
		@Override
		public boolean test(Comparable t1, Comparable t2) {
			return t1.compareTo(t2) > 0;
		}
	},
	/** Greater than or equal operator */
	GE(">=") {
		@Override
		public boolean test(Comparable t1, Comparable t2) {
			return t1.compareTo(t2) >= 0;
		}
	},
	/** Less than operator */
	LT("<") {
		@Override
		public boolean test(Comparable t1, Comparable t2) {
			return t1.compareTo(t2) < 0;
		}
	},
	/** Less than or equal operator */
	LE("<=") {
		@Override
		public boolean test(Comparable t1, Comparable t2) {
			return t1.compareTo(t2) <= 0;
		}
	};

	private final String code;

	private ComparableOperator(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return code;
	}

}

package org.tomvej.fmassoc.plugins.filters.basic.operator;

import java.util.Objects;

/**
 * Operators applicable on all objects.
 * 
 * @author Tomáš Vejpustek
 */
public enum ObjectOperator implements Operator<Object> {
	/** Equality operator */
	EQ("=") {
		@Override
		public boolean test(Object t1, Object t2) {
			return Objects.equals(t1, t2);
		}
	},
	/** non-equality operator */
	NEQ("!=") {
		@Override
		public boolean test(Object t1, Object t2) {
			return !Objects.equals(t1, t2);
		}
	};

	private final String code;

	private ObjectOperator(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return code;
	}
}

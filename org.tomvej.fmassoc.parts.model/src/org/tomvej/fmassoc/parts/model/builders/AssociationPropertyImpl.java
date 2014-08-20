package org.tomvej.fmassoc.parts.model.builders;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Multiplicity;
import org.tomvej.fmassoc.model.db.Table;

class AssociationPropertyImpl extends NamedImpl implements AssociationProperty {
	private final boolean mandatory;
	private final Multiplicity mult;
	private final String reverseName;
	private final Table src, dst;

	AssociationPropertyImpl(String name, String implName, boolean mandatory, Multiplicity mult, String reverseName,
			Table source, Table destination) {
		super(name, implName);
		this.mandatory = mandatory;
		this.mult = Validate.notNull(mult);
		this.reverseName = Validate.notBlank(reverseName);
		src = Validate.notNull(source);
		dst = Validate.notNull(destination);
	}

	@Override
	public Table getSource() {
		return src;
	}

	@Override
	public Table getDestination() {
		return dst;
	}

	@Override
	public Multiplicity getMultiplicity() {
		return mult;
	}

	@Override
	public Table getParent() {
		return src;
	}

	@Override
	public boolean isReverse() {
		return false;
	};

	public boolean isMandatory() {
		return mandatory;
	};

	AssociationProperty getReverse() {
		return new Reverse(this);
	}

	private static class Reverse implements AssociationProperty {
		private final AssociationPropertyImpl inner;

		private Reverse(AssociationPropertyImpl inner) {
			this.inner = inner;
		}

		@Override
		public String getName() {
			return inner.reverseName;
		}

		@Override
		public Table getSource() {
			return inner.getDestination();
		}

		@Override
		public Table getDestination() {
			return inner.getSource();
		}

		@Override
		public boolean isReverse() {
			return true;
		}

		// delegate methods

		@Override
		public Multiplicity getMultiplicity() {
			return inner.getMultiplicity();
		}

		@Override
		public Table getParent() {
			return inner.getParent();
		}

		@Override
		public boolean isMandatory() {
			return inner.isMandatory();
		}

		@Override
		public String getImplName() {
			return inner.getImplName();
		}

	}
}

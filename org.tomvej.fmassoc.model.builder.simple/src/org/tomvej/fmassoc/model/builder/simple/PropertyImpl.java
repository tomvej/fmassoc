package org.tomvej.fmassoc.model.builder.simple;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;

class PropertyImpl implements Property {
	private final String name, implName;
	private final Table parent;

	PropertyImpl(String name, String implName, Table parent) {
		this.name = Validate.notBlank(name);
		this.implName = Validate.notBlank(implName);
		this.parent = Validate.notNull(parent);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getImplName() {
		return implName;
	}

	@Override
	public Table getParent() {
		return parent;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getParent().getName() + "." + getName() + "]";
	}
}

package org.tomvej.fmassoc.parts.model.builders;

import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;


public class PropertyBuilder {
	private String name, implName;

	public PropertyBuilder() {}

	public PropertyBuilder(String name, String implName) {
		this.name = name;
		this.implName = implName;
	}

	public String getName() {
		return name;
	}

	public PropertyBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public String getImplName() {
		return implName;
	}

	public PropertyBuilder setImplName(String implName) {
		this.implName = implName;
		return this;
	}

	Property create(Table parent) {
		return new PropertyImpl(getName(), getImplName(), parent);
	}
}

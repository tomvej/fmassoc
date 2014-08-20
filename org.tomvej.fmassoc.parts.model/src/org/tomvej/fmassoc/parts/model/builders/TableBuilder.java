package org.tomvej.fmassoc.parts.model.builders;

import org.apache.commons.lang3.Validate;

public class TableBuilder {
	private Integer number;
	private String name, implName, idImplName;

	public TableBuilder() {}

	public TableBuilder(Integer number, String name, String implName, String idImplName) {
		this.number = number;
		this.name = name;
		this.implName = implName;
		this.idImplName = idImplName;
	}

	public int getNumber() {
		return number;
	}

	public TableBuilder setNumber(int number) {
		this.number = number;
		return this;
	}

	public String getName() {
		return name;
	}

	public TableBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public String getImplName() {
		return implName;
	}

	public TableBuilder setImplName(String implName) {
		this.implName = implName;
		return this;
	}

	public String getIdImplName() {
		return idImplName;
	}

	public TableBuilder setIdImplName(String idImplName) {
		this.idImplName = idImplName;
		return this;
	}

	public TableBuilder setNumber(Integer number) {
		this.number = number;
		return this;
	}

	TableImpl create() {
		Validate.validState(name != null, "Name must be set.");
		return new TableImpl(getName(), getImplName(), getIdImplName(), getNumber());
	}
}

package org.tomvej.fmassoc.parts.model.builders;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.Multiplicity;
import org.tomvej.fmassoc.model.db.Table;

public class AssociationBuilder {
	private String name, implName, reverseName;
	private Multiplicity mult;
	private Boolean optional;

	public AssociationBuilder() {}

	public AssociationBuilder(String name, String implName, Multiplicity mult, Boolean optional, String reverseName) {
		this.name = name;
		this.implName = implName;
		this.mult = mult;
		this.optional = optional;
		this.reverseName = reverseName;
	}

	public String getName() {
		return name;
	}

	public AssociationBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public String getImplName() {
		return implName;
	}

	public AssociationBuilder setImplName(String implName) {
		this.implName = implName;
		return this;
	}

	public Multiplicity getMultiplicity() {
		return mult;
	}

	public AssociationBuilder setMultiplicity(Multiplicity mult) {
		this.mult = mult;
		return this;
	}

	public boolean isMandatory() {
		return optional;
	}

	public AssociationBuilder setMandatory(boolean optional) {
		this.optional = optional;
		return this;
	}

	public String getReverseName() {
		return reverseName;
	}

	public AssociationBuilder setReverseName(String reverseName) {
		this.reverseName = reverseName;
		return this;
	}

	AssociationPropertyImpl create(Table source, Table destination) {
		Validate.validState(optional != null, "Optionality must be set.");
		return new AssociationPropertyImpl(getName(), getImplName(), isMandatory(), getMultiplicity(), getReverseName(),
				source, destination);
	}
}

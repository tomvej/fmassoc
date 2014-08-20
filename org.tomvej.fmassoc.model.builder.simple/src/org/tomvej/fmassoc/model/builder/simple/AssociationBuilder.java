package org.tomvej.fmassoc.model.builder.simple;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Multiplicity;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Builder for {@link AssociationProperty}.
 * 
 * @author Tomáš Vejpustek
 */
public class AssociationBuilder {
	private String name, implName, reverseName;
	private Multiplicity mult;
	private Boolean mandatory;

	/**
	 * Initialize no values.
	 */
	public AssociationBuilder() {}

	/**
	 * Initialize all values.
	 */
	public AssociationBuilder(String name, String implName, Multiplicity mult, boolean mandatory, String reverseName) {
		this.name = name;
		this.implName = implName;
		this.mult = mult;
		this.mandatory = mandatory;
		this.reverseName = reverseName;
	}

	/**
	 * Set logical name.
	 * 
	 * @see AssociationProperty#getName()
	 * 
	 * @return This builder.
	 * 
	 */
	public AssociationBuilder setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Set database name.
	 * 
	 * @return This builder.
	 * @see AssociationProperty#getImplName()
	 */
	public AssociationBuilder setImplName(String implName) {
		this.implName = implName;
		return this;
	}

	/**
	 * Set multiplicity.
	 * 
	 * @return This builder.
	 * @see AssociationProperty#getMultiplicity()
	 */
	public AssociationBuilder setMultiplicity(Multiplicity mult) {
		this.mult = mult;
		return this;
	}


	/**
	 * Set whether the association is mandatory.
	 * 
	 * @return This builder.
	 * @see AssociationProperty#isMandatory()
	 */
	public AssociationBuilder setMandatory(boolean optional) {
		this.mandatory = optional;
		return this;
	}

	/**
	 * Set logical name of the reverse association.
	 * 
	 * @return This builder.
	 * @see AssociationProperty#getName()
	 * @see AssociationProperty#isReverse()
	 */
	public AssociationBuilder setReverseName(String reverseName) {
		this.reverseName = reverseName;
		return this;
	}

	AssociationPropertyImpl create(Table source, Table destination) {
		Validate.validState(mandatory != null, "Optionality must be set.");
		return new AssociationPropertyImpl(name, implName, mandatory, mult, reverseName, source, destination);
	}
}

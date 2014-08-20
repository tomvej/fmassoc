package org.tomvej.fmassoc.parts.model.builders;

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
	public AssociationBuilder(String name, String implName, Multiplicity mult, Boolean mandatory, String reverseName) {
		this.name = name;
		this.implName = implName;
		this.mult = mult;
		this.mandatory = mandatory;
		this.reverseName = reverseName;
	}

	/**
	 * Get logical name.
	 * 
	 * @see AssociationProperty#getName()
	 */
	public String getName() {
		return name;
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
	 * Get database name.
	 * 
	 * @see AssociationProperty#getImplName()
	 */
	public String getImplName() {
		return implName;
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
	 * Get multiplicity.
	 * 
	 * @see AssociationProperty#getMultiplicity()
	 */
	public Multiplicity getMultiplicity() {
		return mult;
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
	 * Get whether the association is mandatory.
	 * 
	 * @see AssociationProperty#isMandatory()
	 */
	public boolean isMandatory() {
		return mandatory;
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
	 * Get logical name of the reverse association.
	 * 
	 * @see AssociationProperty#getName()
	 * @see AssociationProperty#isReverse()
	 */
	public String getReverseName() {
		return reverseName;
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
		return new AssociationPropertyImpl(getName(), getImplName(), isMandatory(), getMultiplicity(), getReverseName(),
				source, destination);
	}
}

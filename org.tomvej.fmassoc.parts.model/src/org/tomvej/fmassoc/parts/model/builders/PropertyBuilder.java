package org.tomvej.fmassoc.parts.model.builders;

import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Builder for {@link Property}.
 * 
 * @author Tomáš Vejpustek
 */
public class PropertyBuilder {
	private String name, implName;

	/**
	 * Initialize no values.
	 */
	public PropertyBuilder() {}

	/**
	 * Initialize all values.
	 */
	public PropertyBuilder(String name, String implName) {
		this.name = name;
		this.implName = implName;
	}

	/**
	 * Get logical name.
	 *
	 * @see Property#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set logical name.
	 * 
	 * @return This builder.
	 * @see Property#getName()
	 */
	public PropertyBuilder setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get database name.
	 * 
	 * @see Property#getImplName()
	 */
	public String getImplName() {
		return implName;
	}

	/**
	 * Set database name.
	 * 
	 * @return This builder.
	 * @see Property#getImplName()
	 */
	public PropertyBuilder setImplName(String implName) {
		this.implName = implName;
		return this;
	}

	Property create(Table parent) {
		return new PropertyImpl(getName(), getImplName(), parent);
	}
}

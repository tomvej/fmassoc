package org.tomvej.fmassoc.parts.model.builders;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Builder for {@link Table}.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class TableBuilder {
	private Integer number;
	private String name, implName, idImplName;

	/**
	 * Initialize no values.
	 */
	public TableBuilder() {}

	/**
	 * Initialize all values.
	 */
	public TableBuilder(Integer number, String name, String implName, String idImplName) {
		this.number = number;
		this.name = name;
		this.implName = implName;
		this.idImplName = idImplName;
	}

	/**
	 * Get number.
	 * 
	 * @see Table#getNumber()
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Set number.
	 * 
	 * @return This builder.
	 * @see Table#getNumber()
	 */
	public TableBuilder setNumber(int number) {
		this.number = number;
		return this;
	}

	/**
	 * Get logical name.
	 * 
	 * @see Table#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set logical name.
	 * 
	 * @return This builder.
	 * @see Table#getName()
	 */
	public TableBuilder setName(String name) {
		this.name = name;
		return this;
	}

	/**
	 * Get database name.
	 * 
	 * @see Table#getImplName()
	 */
	public String getImplName() {
		return implName;
	}

	/**
	 * Set database name.
	 * 
	 * @return This builder.
	 * @see Table#getImplName()
	 */
	public TableBuilder setImplName(String implName) {
		this.implName = implName;
		return this;
	}

	/**
	 * Get database name of the primary key column.
	 * 
	 * @see Table#getIDImplName()
	 */
	public String getIdImplName() {
		return idImplName;
	}

	/**
	 * Set database name of the primary key column.
	 * 
	 * @return This builder.
	 * @see Table#getIDImplName()
	 */
	public TableBuilder setIdImplName(String idImplName) {
		this.idImplName = idImplName;
		return this;
	}

	TableImpl create() {
		Validate.validState(name != null, "Name must be set.");
		return new TableImpl(getName(), getImplName(), getIdImplName(), getNumber());
	}
}

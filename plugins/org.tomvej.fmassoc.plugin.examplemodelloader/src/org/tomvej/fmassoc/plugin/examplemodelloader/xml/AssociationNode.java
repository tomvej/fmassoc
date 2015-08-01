package org.tomvej.fmassoc.plugin.examplemodelloader.xml;

import javax.xml.bind.annotation.XmlAttribute;

import org.tomvej.fmassoc.parts.model.ModelLoadingException;

/**
 * XML element for association between tables.
 * 
 * @author Tomáš Vejpustek
 */
public class AssociationNode {
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String reverse;
	@XmlAttribute
	private String table;
	@XmlAttribute
	private Boolean mandatory;
	@XmlAttribute
	private Boolean unique;

	/**
	 * Return association name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return inverse association name.
	 */
	public String getReverse() {
		return reverse;
	}

	/**
	 * Return destination table name.
	 */
	public String getTable() {
		return table;
	}

	/**
	 * Return whether this association is mandatory.
	 */
	public boolean isMandatory() {
		return mandatory != null && mandatory;
	}

	/**
	 * Return each destination table row can be associated with only one row of
	 * this table.
	 */
	public boolean isUnique() {
		return unique != null && unique;
	}

	/**
	 * Validate association fields.
	 */
	public void validate() throws ModelLoadingException {
		if (name == null) {
			throw new ModelLoadingException("Unknown association name");
		}
		if (table == null) {
			throw new ModelLoadingException("Unknown destination table for association: " + name);
		}
		if (reverse == null) {
			throw new ModelLoadingException("Unknown resverse name for association: " + name);
		}
	}
}

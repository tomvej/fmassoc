package org.tomvej.fmassoc.plugin.examplemodelloader.xml;

import javax.xml.bind.annotation.XmlAttribute;

import org.tomvej.fmassoc.parts.model.ModelLoadingException;

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

	public String getName() {
		return name;
	}

	public String getReverse() {
		return reverse;
	}

	public String getTable() {
		return table;
	}

	public boolean isMandatory() {
		return mandatory != null && mandatory;
	}

	public boolean isUnique() {
		return unique != null && unique;
	}

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

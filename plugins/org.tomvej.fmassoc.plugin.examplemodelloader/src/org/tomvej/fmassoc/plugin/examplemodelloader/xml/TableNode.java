package org.tomvej.fmassoc.plugin.examplemodelloader.xml;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.tomvej.fmassoc.parts.model.ModelLoadingException;

public class TableNode {
	@XmlAttribute
	private String name;
	@XmlAttribute
	private Boolean forbidden;
	@XmlElement(name = "property")
	private List<PropertyNode> properties;
	@XmlElement(name = "association")
	private List<AssociationNode> associations;

	public String getName() {
		return name;
	}

	public boolean isForbidden() {
		return forbidden != null && forbidden;
	}

	public List<PropertyNode> getProperties() {
		if (properties == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(properties);
	}

	public List<AssociationNode> getAssociation() {
		if (associations == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(associations);
	}

	public void validate() throws ModelLoadingException {
		if (name == null) {
			throw new ModelLoadingException("Unknown table name.");
		}
	}

}

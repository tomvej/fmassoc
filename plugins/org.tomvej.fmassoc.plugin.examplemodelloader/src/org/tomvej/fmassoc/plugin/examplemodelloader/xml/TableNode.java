package org.tomvej.fmassoc.plugin.examplemodelloader.xml;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.tomvej.fmassoc.parts.model.ModelLoadingException;

/**
 * XML element containing a table, its associations and properties.
 * 
 * @author Tomáš Vejpustek
 */
public class TableNode {
	@XmlAttribute
	private String name;
	@XmlAttribute
	private Boolean forbidden;
	@XmlElement(name = "property")
	private List<PropertyNode> properties;
	@XmlElement(name = "association")
	private List<AssociationNode> associations;

	/**
	 * Return table name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return whether this table is a forbidden table.
	 */
	public boolean isForbidden() {
		return forbidden != null && forbidden;
	}

	/**
	 * Return properties XML elements.
	 */
	public List<PropertyNode> getProperties() {
		if (properties == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(properties);
	}

	/**
	 * Return associations XML elements.
	 */
	public List<AssociationNode> getAssociation() {
		if (associations == null) {
			return Collections.emptyList();
		}
		return Collections.unmodifiableList(associations);
	}

	/**
	 * Validate fields of this table.
	 */
	public void validate() throws ModelLoadingException {
		if (name == null) {
			throw new ModelLoadingException("Unknown table name.");
		}
	}

}

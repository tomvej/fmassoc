package org.tomvej.fmassoc.plugin.mobilemodelloader.xml;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlPath;

/**
 * XML node corresponding to the type/table.
 * 
 * @author Tomáš Vejpustek
 */
public class TypeNode {
	private static final String OID_COLUMN = "@serversql_oid_column";
	@XmlAttribute
	private String name;
	@XmlAttribute(name = "type_number")
	private Integer number;
	@XmlPath(DataModelNode.IMPL_NAME_XML_PATH)
	private String implName;
	@XmlElement(name = "property")
	private List<PropertyNode> properties;
	@XmlElement(name = "association")
	private List<AssociationNode> associations;
	@XmlPath("root_without_subtypes/" + OID_COLUMN + "|root_with_subtypes/" + OID_COLUMN + "|final_subtype/" + OID_COLUMN)
	private String oid;


	/**
	 * Return human-readable name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return table number.
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Return implementation name.
	 */
	public String getImplName() {
		return implName;
	}

	/**
	 * Return list of properties.
	 */
	public List<PropertyNode> getProperties() {
		return Collections.unmodifiableList(properties);
	}

	/**
	 * Return list of outgoing associations.
	 */
	public List<AssociationNode> getAssociations() {
		return Collections.unmodifiableList(associations);
	}

	/**
	 * Return name of the primary key column.
	 */
	public String getOIDColumn() {
		return oid;
	}

}

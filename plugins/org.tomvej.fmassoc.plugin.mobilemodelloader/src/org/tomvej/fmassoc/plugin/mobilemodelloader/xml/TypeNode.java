package org.tomvej.fmassoc.plugin.mobilemodelloader.xml;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;

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
	private List<PropertyNode> properties = Collections.emptyList();
	@XmlElement(name = "association")
	private List<AssociationNode> associations = Collections.emptyList();
	@XmlPath("root_without_subtypes/" + OID_COLUMN)
	private String oid1;
	@XmlPath("root_with_subtypes/" + OID_COLUMN)
	private String oid2;
	@XmlPath("final_subtype/" + OID_COLUMN)
	private String oid3;


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
		if (oid1 != null) {
			return oid1;
		}
		if (oid2 != null) {
			return oid2;
		}
		if (oid3 != null) {
			return oid3;
		}
		return null;
	}

	/**
	 * Validates whether this object is well-defined.
	 * 
	 * @throws ModelLoadingException
	 *             when some properties are missing.
	 */
	public void validate() throws ModelLoadingException {
		if (name == null) {
			error("Unable to read name of table.");
		}
		if (implName == null) {
			error("Unable to read implementation name of table `" + name + "'.");
		}
		if (number == null) {
			error("Unable to read number of table `" + name + "'.");
		}
		if (oid1 == null && oid2 == null && oid3 == null) {
			error("Unable to read object id column of table `" + name + "'.");
		}
	}

	private void error(String message) throws ModelLoadingException {
		throw new ModelLoadingException(message);
	}

}

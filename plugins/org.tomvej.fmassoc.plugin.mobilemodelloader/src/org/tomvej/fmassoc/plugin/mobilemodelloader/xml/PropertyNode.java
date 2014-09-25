package org.tomvej.fmassoc.plugin.mobilemodelloader.xml;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;

/**
 * XML node corresponding to property.
 * 
 * @author Tomáš Vejpustek
 */
public class PropertyNode {
	@XmlAttribute
	private String name;
	@XmlPath(DataModelNode.IMPL_NAME_XML_PATH)
	private String implName;
	@XmlAttribute
	private String datatype;
	@XmlAttribute
	private Optional opt;

	/**
	 * Return human-readable name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return implementation name.
	 */
	public String getImplName() {
		return implName;
	}

	/**
	 * Is this property association to blob.
	 */
	public boolean isBlob() {
		return "BLOB".equals(datatype);
	}

	/**
	 * Is this property mandatory.
	 */
	public boolean isMandatory() {
		return opt.mandatory;
	}

	/**
	 * Validates whether this object is well-defined.
	 * 
	 * @throws ModelLoadingException
	 *             when some properties are missing.
	 */
	public void validate() throws ModelLoadingException {
		if (name == null) {
			throw new ModelLoadingException("Unable to read property name.");
		}
		if (implName == null) {
			throw new ModelLoadingException("Unable to read implementation name of property `" + name + "'.");
		}
		if (datatype == null) {
			throw new ModelLoadingException("Unable to read data type of property `" + name + "'.");
		}
		if (opt == null) {
			throw new ModelLoadingException("Unable to read whether property `" + name + "' is mandatory.");
		}
	}
}

package org.tomvej.fmassoc.plugin.mobilemodelloader.xml;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.persistence.oxm.annotations.XmlPath;

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

}

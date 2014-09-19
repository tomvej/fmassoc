package org.tomvej.fmassoc.plugin.mobilemodelloader.xml;

import javax.xml.bind.annotation.XmlAttribute;

import org.eclipse.persistence.oxm.annotations.XmlPath;

public class PropertyNode {
	@XmlAttribute
	private String name;
	@XmlPath(DataModelNode.IMPL_NAME_XML_PATH)
	private String implName;

	public String getName() {
		return name;
	}

	public String getImplName() {
		return implName;
	}

}

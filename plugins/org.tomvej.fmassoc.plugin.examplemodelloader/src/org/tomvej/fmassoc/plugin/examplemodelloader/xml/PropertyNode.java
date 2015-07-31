package org.tomvej.fmassoc.plugin.examplemodelloader.xml;

import javax.xml.bind.annotation.XmlAttribute;

import org.tomvej.fmassoc.parts.model.ModelLoadingException;


/**
 * XML element for table property.
 * 
 * @author Tomáš Vejpustek
 */
public class PropertyNode {
	@XmlAttribute
	private String name;

	/**
	 * Return property name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Validate fields of this property.
	 */
	public void validate() throws ModelLoadingException {
		if (name == null) {
			throw new ModelLoadingException("Unknown property name.");
		}
	}

}

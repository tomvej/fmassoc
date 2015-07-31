package org.tomvej.fmassoc.plugin.examplemodelloader.xml;

import javax.xml.bind.annotation.XmlAttribute;

import org.tomvej.fmassoc.parts.model.ModelLoadingException;


public class PropertyNode {
	@XmlAttribute
	private String name;

	public String getName() {
		return name;
	}

	public void validate() throws ModelLoadingException {
		if (name == null) {
			throw new ModelLoadingException("Unknown property name.");
		}
	}

}

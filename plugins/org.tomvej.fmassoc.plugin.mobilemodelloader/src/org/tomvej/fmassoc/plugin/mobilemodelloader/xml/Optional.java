package org.tomvej.fmassoc.plugin.mobilemodelloader.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

/**
 * XML attribute type containing association/property optionality.
 * 
 * @author Tomáš Vejpustek
 */
@XmlType
@XmlEnum
public enum Optional {
	/** Association/property is optional. */
	@XmlEnumValue("OPTIONAL")
	OPTIONAL(false),
	/** Association/property is mandatory. */
	@XmlEnumValue("MANDATORY")
	MANDATORY(true);

	/** Whether association/property is mandatory. */
	public final boolean mandatory;

	private Optional(boolean mandatory) {
		this.mandatory = mandatory;
	}
}
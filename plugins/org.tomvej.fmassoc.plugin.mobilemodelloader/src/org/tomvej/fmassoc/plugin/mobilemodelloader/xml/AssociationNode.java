package org.tomvej.fmassoc.plugin.mobilemodelloader.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

import org.eclipse.persistence.oxm.annotations.XmlPath;
import org.tomvej.fmassoc.model.db.Multiplicity;

/**
 * XML node corresponding to association.
 * 
 * @author Tomáš Vejpustek
 */
public class AssociationNode {

	@XmlType
	@XmlEnum
	private enum Optional {
		@XmlEnumValue("OPTIONAL")
		OPTIONAL(false),
		@XmlEnumValue("MANDATORY")
		MANDATORY(true);

		public final boolean mandatory;

		private Optional(boolean mandatory) {
			this.mandatory = mandatory;
		}
	}

	@XmlType
	@XmlEnum
	private enum Mult {
		@XmlEnumValue("TO_MANY")
		TO_MANY(Multiplicity.MANY_TO_ONE), // it's reverse!
		@XmlEnumValue("TO_ONE")
		TO_ONE(Multiplicity.ONE_TO_ONE);

		public final Multiplicity multiplicity;

		private Mult(Multiplicity multiplicity) {
			this.multiplicity = multiplicity;
		}
	}

	@XmlPath("forward/@name")
	private String name;
	@XmlPath("reverse/@name")
	private String reverseName;
	@XmlPath(DataModelNode.IMPL_NAME_XML_PATH)
	private String implName;
	@XmlPath("forward/@type")
	private String target;
	@XmlPath("forward/@opt")
	private Optional optional;
	@XmlPath("reverse/@mult")
	private Mult multiplicity;

	/**
	 * Return human-readable name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return human-readable name of the reverse association.
	 */
	public String getReverseName() {
		return reverseName;
	}

	/**
	 * Return implementation name.
	 */
	public String getImplName() {
		return implName;
	}

	/**
	 * Return name of the target table.
	 */
	public String getTargetName() {
		return target;
	}

	/**
	 * Return whether this association is mandatory.
	 */
	public boolean isMandatory() {
		return optional.mandatory;
	}

	/**
	 * Return this association multiplicity.
	 */
	public Multiplicity getMultiplicity() {
		return multiplicity.multiplicity;
	}

}
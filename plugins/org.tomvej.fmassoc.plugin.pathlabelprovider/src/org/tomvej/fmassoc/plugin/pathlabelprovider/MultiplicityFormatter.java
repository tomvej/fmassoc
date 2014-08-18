package org.tomvej.fmassoc.plugin.pathlabelprovider;

import java.util.EnumMap;

import org.tomvej.fmassoc.model.db.Multiplicity;

/**
 * Stores strings for each of multiplicities.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class MultiplicityFormatter {
	private final EnumMap<Multiplicity, String> strings = new EnumMap<>(Multiplicity.class);

	/**
	 * Set string for multiplicity. Can be chained.
	 * 
	 * @return This provider
	 */
	public MultiplicityFormatter add(Multiplicity mult, String string) {
		strings.put(mult, string);
		return this;
	}

	/**
	 * Convert multiplicity to string.
	 */
	public String toString(Multiplicity target) {
		String result = strings.get(target);
		if (result == null) {
			throw new IllegalArgumentException("Unknown multiplicity: " + target);
		}
		return result;
	}
}

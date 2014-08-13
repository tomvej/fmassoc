package org.tomvej.fmassoc.plugin.pathlabelprovider;

import java.util.EnumMap;

import org.tomvej.fmassoc.model.db.Multiplicity;

public class MultiplicityFormatter {
	private final EnumMap<Multiplicity, String> strings = new EnumMap<>(Multiplicity.class);

	public MultiplicityFormatter add(Multiplicity mult, String string) {
		strings.put(mult, string);
		return this;
	}

	public String toString(Multiplicity target) {
		String result = strings.get(target);
		if (result == null) {
			throw new IllegalArgumentException("Unknown multiplicity: " + target);
		}
		return result;
	}
}

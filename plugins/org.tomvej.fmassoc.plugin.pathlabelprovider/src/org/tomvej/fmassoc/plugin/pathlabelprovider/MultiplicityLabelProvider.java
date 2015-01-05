package org.tomvej.fmassoc.plugin.pathlabelprovider;

import org.tomvej.fmassoc.model.db.Multiplicity;
import org.tomvej.fmassoc.parts.paths.labelprovider.CustomColumnLabelProvider;

/**
 * Multiplicity formatter using "1:1" type expressions.
 * 
 * @author Tomáš Vejpustek
 */
public class MultiplicityLabelProvider implements CustomColumnLabelProvider<Multiplicity> {
	private final MultiplicityFormatter formatter;

	/**
	 * Initialize formatter
	 */
	public MultiplicityLabelProvider() {
		formatter = new MultiplicityFormatter().add(Multiplicity.ONE_TO_ONE, "1:1").
				add(Multiplicity.ONE_TO_MANY, "1:N").add(Multiplicity.MANY_TO_ONE, "N:1").
				add(Multiplicity.MANY_TO_MANY, "M:N");
	}

	public String getText(Multiplicity element) {
		return formatter.toString(element);
	};
}

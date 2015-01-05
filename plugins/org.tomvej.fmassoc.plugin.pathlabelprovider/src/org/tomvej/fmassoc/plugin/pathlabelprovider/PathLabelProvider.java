package org.tomvej.fmassoc.plugin.pathlabelprovider;

import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Multiplicity;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.paths.labelprovider.TextPathLabelProvider;

/**
 * Displays path as list of comprising tables separated by =, <, > according to
 * multiplicity. Tooltip is displayed vertically with association names and
 * multiplicities.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class PathLabelProvider extends TextPathLabelProvider {
	private static final MultiplicityFormatter ARROW_FORMATTER = new MultiplicityFormatter().
			add(Multiplicity.ONE_TO_ONE, "=").add(Multiplicity.ONE_TO_MANY, "<")
			.add(Multiplicity.MANY_TO_ONE, ">").add(Multiplicity.MANY_TO_MANY, "x");
	private static final MultiplicityFormatter VERTICAL_FORMATTER = new MultiplicityFormatter().
			add(Multiplicity.ONE_TO_ONE, "||").add(Multiplicity.ONE_TO_MANY, "/\\").
			add(Multiplicity.MANY_TO_ONE, "V").add(Multiplicity.MANY_TO_MANY, "X");

	@Override
	protected String getTableText(Table target) {
		return target.getImplName();
	}

	@Override
	protected String getAssociationText(AssociationProperty target) {
		String arrow = ARROW_FORMATTER.toString(target.getMultiplicity());
		StringBuilder result = new StringBuilder(" ").append(arrow);
		if (target.isMandatory()) {
			result.append(arrow);
		}
		return result.append(" ").toString();
	}

	@Override
	protected String getTableToolTipText(Table target) {
		return target.getImplName() + " (" + target.getName() + ")\n";
	}

	@Override
	protected String getAssociationToolTipText(AssociationProperty target) {
		StringBuilder result = new StringBuilder("   ");
		if (target.isMandatory()) {
			result.append("(");
		}
		result.append(VERTICAL_FORMATTER.toString(target.getMultiplicity()));
		if (target.isMandatory()) {
			result.append(")");
		}
		return result.append(" ").append(target.getName()).append("\n").toString();
	}
}

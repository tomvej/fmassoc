package org.tomvej.fmassoc.plugin.pathlabelprovider;

import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.paths.labelprovider.TextPathLabelProvider;

public class PathLabelProvider extends TextPathLabelProvider {

	@Override
	protected String getTableText(Table target) {
		return target.getImplName();
	}

	@Override
	protected String getAssociationText(AssociationProperty target) {
		// TODO add formatter
		return " " + " ";
	}

	@Override
	protected String getTableToolTipText(Table target) {
		return target.getImplName() + " (" + target.getName() + ")\n";
	}

	@Override
	protected String getAssociationToolTipText(AssociationProperty target) {
		// TODO add formatter
		return "   " + " " + target.getName() + "\n";
	}
}

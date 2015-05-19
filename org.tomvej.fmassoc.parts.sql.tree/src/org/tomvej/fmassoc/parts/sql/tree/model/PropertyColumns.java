package org.tomvej.fmassoc.parts.sql.tree.model;

import org.tomvej.fmassoc.model.db.Table;

public class PropertyColumns extends TableChild {

	public PropertyColumns(Table parent) {
		super(parent);
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Object[] getChildren() {
		return getParent().getProperties().stream().filter(p -> !VersionProperties.isVersionProperty(p)).toArray();
	}

}

package org.tomvej.fmassoc.parts.sql.tree.model;

import org.tomvej.fmassoc.model.db.Table;

/**
 * Proxy tree element containing all version properties of a table.
 * 
 * @author Tomáš Vejpustek
 */
public class VersionColumns extends TableChild {

	VersionColumns(Table parent) {
		super(parent);
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Object[] getChildren() {
		return getParent().getProperties().stream().filter(p -> VersionProperties.isVersionProperty(p)).toArray();
	}
}

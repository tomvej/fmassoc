package org.tomvej.fmassoc.parts.sql.tree.model;

import org.tomvej.fmassoc.model.db.Table;

/**
 * Proxy tree element containing all property columns of a table (without
 * version properties).
 * 
 * @author Tomáš Vejpustek
 */
public class PropertyColumns extends TableChild {

	PropertyColumns(Table parent) {
		super(parent);
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

	@Override
	public Object[] getChildren() {
		return getParent().getProperties().toArray();
	}

}

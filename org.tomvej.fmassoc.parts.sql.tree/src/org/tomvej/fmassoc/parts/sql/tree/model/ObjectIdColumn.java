package org.tomvej.fmassoc.parts.sql.tree.model;

import org.tomvej.fmassoc.model.db.Table;

/**
 * ID_OBJECT column of a table.
 * 
 * @author Tomáš Vejpustek
 */
public class ObjectIdColumn extends TableChild {

	ObjectIdColumn(Table parent) {
		super(parent);
	}

	@Override
	public boolean hasChildren() {
		return false;
	}

	@Override
	public Object[] getChildren() {
		return null;
	}
}
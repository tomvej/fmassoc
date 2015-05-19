package org.tomvej.fmassoc.parts.sql.tree.model;

import org.tomvej.fmassoc.model.db.Table;

/**
 * Proxy tree element containing all association columns of a table.
 * 
 * @author Tomáš Vejpustek
 */
public class AssociationColumns extends TableChild {

	AssociationColumns(Table parent) {
		super(parent);
	}

	@Override
	public Object[] getChildren() {
		return getParent().getAssociations().stream().filter(a -> !a.isReverse()).toArray();
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

}

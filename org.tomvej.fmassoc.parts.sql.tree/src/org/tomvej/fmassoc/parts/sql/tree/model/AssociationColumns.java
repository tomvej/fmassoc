package org.tomvej.fmassoc.parts.sql.tree.model;

import org.tomvej.fmassoc.model.db.Table;

public class AssociationColumns extends TableChild implements TreeNode {

	public AssociationColumns(Table parent) {
		super(parent);
	}

	@Override
	public Object[] getChildren() {
		return getParent().getAssociations().toArray();
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

}

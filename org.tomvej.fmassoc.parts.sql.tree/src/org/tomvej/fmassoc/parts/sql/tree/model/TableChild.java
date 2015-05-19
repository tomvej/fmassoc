package org.tomvej.fmassoc.parts.sql.tree.model;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.Table;

public abstract class TableChild implements TreeNode {
	private final Table parent;

	public TableChild(Table parent) {
		this.parent = Validate.notNull(parent);
	}

	@Override
	public Table getParent() {
		return parent;
	};

}

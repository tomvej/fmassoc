package org.tomvej.fmassoc.parts.sql.tree.model;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Abstract implementation of {@link TreeNode} for children of {@link Table}.
 * 
 * @author Tomáš Vejpustek
 */
abstract class TableChild implements TreeNode {
	private final Table parent;

	TableChild(Table parent) {
		this.parent = Validate.notNull(parent);
	}

	@Override
	public Table getParent() {
		return parent;
	};

}

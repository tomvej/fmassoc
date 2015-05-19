package org.tomvej.fmassoc.parts.sql.tree.model;

public interface TreeNode {
	Object getParent();

	Object[] getChildren();

	default boolean hasChildren() {
		return getChildren().length > 0;
	}
}

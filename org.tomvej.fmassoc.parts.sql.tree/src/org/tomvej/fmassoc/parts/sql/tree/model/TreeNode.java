package org.tomvej.fmassoc.parts.sql.tree.model;

/**
 * Node of a tree model. Mostly used for proxy tree elements.
 * 
 * @author Tomáš Vejpustek
 */
public interface TreeNode {
	/**
	 * Returns parent of this element.
	 */
	Object getParent();

	/**
	 * Return children of this element.
	 */
	Object[] getChildren();

	/**
	 * Returns whether this elements has children.
	 * Default implementation uses {@link #getChildren()}.
	 */
	default boolean hasChildren() {
		return getChildren().length > 0;
	}
}

package org.tomvej.fmassoc.parts.sql.tree.model;

import org.tomvej.fmassoc.model.db.Table;

/**
 * ID_OBJECT column of a table. All ID_OBJECT columns for a table should
 * be equal.
 * 
 * @author Tomáš Vejpustek
 */
public class ObjectIdColumn extends TableChild {

	private ObjectIdColumn(Table parent) {
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

	@Override
	public int hashCode() {
		return getParent().hashCode();
	}

	@Override
	public boolean equals(Object target) {
		if (target == this) {
			return true;
		}

		if (target instanceof ObjectIdColumn) {
			return ((ObjectIdColumn) target).getParent().equals(getParent());
		} else {
			return false;
		}
	}

	/**
	 * Returns ID_OBJECT column for given table.
	 */
	public static ObjectIdColumn getInstance(Table parent) {
		return new ObjectIdColumn(parent);
	}
}
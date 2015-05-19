package org.tomvej.fmassoc.parts.sql.tree.model;

import org.tomvej.fmassoc.model.db.Table;

/**
 * ID_OBJECT column of a table.
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
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (obj instanceof ObjectIdColumn) {
			return ((ObjectIdColumn) obj).getParent().equals(getParent());
		} else {
			return false;
		}
	}

	/**
	 * Return an instance of this column.
	 */
	public static ObjectIdColumn getInstance(Table parent) {
		return new ObjectIdColumn(parent);
	}


}

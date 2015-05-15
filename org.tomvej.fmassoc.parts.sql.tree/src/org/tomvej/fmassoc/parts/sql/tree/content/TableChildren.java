package org.tomvej.fmassoc.parts.sql.tree.content;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.sql.tree.model.AssociationColumns;
import org.tomvej.fmassoc.parts.sql.tree.model.ObjectIdColumn;
import org.tomvej.fmassoc.parts.sql.tree.model.PropertyColumns;
import org.tomvej.fmassoc.parts.sql.tree.model.VersionColumns;

public class TableChildren {
	private final ObjectIdColumn objectIdColumn;
	private final PropertyColumns propertyColumns;
	private final VersionColumns versionColumns;
	private final AssociationColumns associationColumns;

	public TableChildren(Table target) {
		Validate.notNull(target);
		objectIdColumn = ObjectIdColumn.getInstance(target);
		associationColumns = new AssociationColumns(target);
		propertyColumns = new PropertyColumns(target);
		versionColumns = new VersionColumns(target);
	}

	public Object[] getChildren() {
		return new Object[] { objectIdColumn, associationColumns, propertyColumns, versionColumns };
	}

	public ObjectIdColumn getObjectIdColumn() {
		return objectIdColumn;
	}

	public PropertyColumns getPropertyColumns() {
		return propertyColumns;
	}

	public VersionColumns getVersionColumns() {
		return versionColumns;
	}

	public AssociationColumns getAssociationColumns() {
		return associationColumns;
	}

}

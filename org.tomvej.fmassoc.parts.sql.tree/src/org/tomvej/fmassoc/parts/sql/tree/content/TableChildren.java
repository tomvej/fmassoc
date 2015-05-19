package org.tomvej.fmassoc.parts.sql.tree.content;

import java.util.ArrayList;
import java.util.List;

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
		AssociationColumns associationColumns = new AssociationColumns(target);
		this.associationColumns = associationColumns.getChildren().length > 0 ? associationColumns : null;
		PropertyColumns propertyColumns = new PropertyColumns(target);
		this.propertyColumns = propertyColumns.getChildren().length > 0 ? propertyColumns : null;
		versionColumns = new VersionColumns(target);
	}

	public Object[] getChildren() {
		List<Object> children = new ArrayList<>();
		children.add(objectIdColumn);
		if (associationColumns != null) {
			children.add(associationColumns);
		}
		if (propertyColumns != null) {
			children.add(propertyColumns);
		}
		children.add(versionColumns);
		return children.toArray();
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

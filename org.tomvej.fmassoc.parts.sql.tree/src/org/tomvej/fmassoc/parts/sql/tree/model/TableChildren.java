package org.tomvej.fmassoc.parts.sql.tree.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Auxiliary structure containing all proxy tree elements attached under a
 * {@link Table}. Contains OID column, associations, properties and version
 * properties.
 * 
 * @author Tomáš Vejpustek
 */
class TableChildren {
	private final ObjectIdColumn objectIdColumn;
	private final PropertyColumns propertyColumns;
	private final AssociationColumns associationColumns;

	/**
	 * Specify parent table.
	 */
	TableChildren(Table target, Collection<AssociationProperty> pathAssociations) {
		Validate.notNull(target);
		objectIdColumn = ObjectIdColumn.getInstance(target);
		AssociationColumns associationColumns = new AssociationColumns(target, pathAssociations);
		this.associationColumns = associationColumns.getChildren().length > 0 ? associationColumns : null;
		PropertyColumns propertyColumns = new PropertyColumns(target);
		this.propertyColumns = propertyColumns.getChildren().length > 0 ? propertyColumns : null;
	}

	/**
	 * Returns all children attached under the table. There are no {@code null}
	 * elements.
	 */
	Object[] getChildren() {
		List<Object> children = new ArrayList<>();
		children.add(objectIdColumn);
		if (associationColumns != null) {
			children.add(associationColumns);
		}
		if (propertyColumns != null) {
			children.add(propertyColumns);
		}
		return children.toArray();
	}

	/**
	 * Returns OID column of the table.
	 */
	ObjectIdColumn getObjectIdColumn() {
		return objectIdColumn;
	}

	/**
	 * Returns all properties of the table (except version properties).
	 */
	PropertyColumns getPropertyColumns() {
		return propertyColumns;
	}

	/**
	 * Return all associations of the table.
	 */
	AssociationColumns getAssociationColumns() {
		return associationColumns;
	}
}

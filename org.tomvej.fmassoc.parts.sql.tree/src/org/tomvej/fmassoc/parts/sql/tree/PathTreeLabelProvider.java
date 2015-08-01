package org.tomvej.fmassoc.parts.sql.tree;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.sql.tree.model.AssociationColumns;
import org.tomvej.fmassoc.parts.sql.tree.model.ObjectIdColumn;
import org.tomvej.fmassoc.parts.sql.tree.model.PropertyColumns;
import org.tomvej.fmassoc.parts.sql.tree.model.VersionColumns;

/**
 * Label provider for path tree.
 * 
 * @author Tomáš Vejpustek
 */
public class PathTreeLabelProvider extends ColumnLabelProvider {
	@Override
	public String getText(Object element) {
		if (element instanceof Table) {
			return ((Table) element).getName() + " (" + ((Table) element).getImplName() + ")";

		} else if (element instanceof AssociationProperty) {
			AssociationProperty property = (AssociationProperty) element;
			return property.getName() + " " + property.getOther().getName();

		} else if (element instanceof Property) {
			return ((Property) element).getName();

		} else if (element instanceof ObjectIdColumn) {
			return ((ObjectIdColumn) element).getParent().getIDImplName();

		} else if (element instanceof AssociationColumns) {
			return "associations";

		} else if (element instanceof PropertyColumns) {
			return "properties";

		} else if (element instanceof VersionColumns) {
			return "version properties";
		}

		throw new IllegalArgumentException("Unknown element type: " + element.getClass());
	}
}

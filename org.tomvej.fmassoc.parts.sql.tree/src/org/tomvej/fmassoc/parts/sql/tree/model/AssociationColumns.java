package org.tomvej.fmassoc.parts.sql.tree.model;

import java.util.Collection;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Proxy tree element containing all association columns of a table.
 * 
 * @author Tomáš Vejpustek
 */
public class AssociationColumns extends TableChild {
	private final Collection<AssociationProperty> pathProperties;

	AssociationColumns(Table parent, Collection<AssociationProperty> pathProperties) {
		super(parent);
		this.pathProperties = Validate.notNull(pathProperties);
	}

	@Override
	public Object[] getChildren() {
		return getParent().getAssociations().stream().
				filter(a -> !a.isReverse()).
				filter(a -> !pathProperties.contains(a))
				.toArray();
	}

	@Override
	public boolean hasChildren() {
		return true;
	}

}

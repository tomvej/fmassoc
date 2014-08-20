package org.tomvej.fmassoc.parts.model.builders;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.DataModel;


public class DataModelBuilder {
	private Set<TableImpl> tables = new HashSet<>();

	public TableImpl addTable(TableBuilder builder) {
		TableImpl result = builder.create();
		if (tables.contains(result)) {
			return null;
		} else {
			return result;
		}
	}

	public void addProperty(PropertyBuilder builder, TableImpl parent) {
		Validate.isTrue(tables.contains(parent), "Cannot add property to table not in this data model.");
		parent.addProperty(builder.create(parent));
	}

	public void addAssociation(AssociationBuilder builder, TableImpl source, TableImpl destination) {
		Validate.isTrue(tables.contains(source) && tables.contains(destination));
		AssociationPropertyImpl target = builder.create(source, destination);
		source.addAssociation(target);
		destination.addAssociation(target.getReverse());
	}

	public DataModel create() {
		return new DataModelImpl(tables);
	}
}

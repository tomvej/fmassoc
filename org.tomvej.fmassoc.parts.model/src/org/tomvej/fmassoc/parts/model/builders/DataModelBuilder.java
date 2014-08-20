package org.tomvej.fmassoc.parts.model.builders;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.DataModel;

/**
 * Builder for {@link DataModel}.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class DataModelBuilder {
	private Set<TableImpl> tables = new HashSet<>();

	/**
	 * Add a table.
	 * 
	 * @param builder
	 *            Specifies table information; is copied.
	 * @return Resulting table.
	 */
	public TableImpl addTable(TableBuilder builder) {
		TableImpl result = builder.create();
		if (tables.contains(result)) {
			return null;
		} else {
			return result;
		}
	}

	/**
	 * Add a property.
	 * 
	 * @param builder
	 *            Specifies property information; is copied.
	 * @param parent
	 *            Parent table of the property. Must have been previously added.
	 */
	public void addProperty(PropertyBuilder builder, TableImpl parent) {
		Validate.isTrue(tables.contains(parent), "Cannot add property to table not in this data model.");
		parent.addProperty(builder.create(parent));
	}

	/**
	 * Add an association.
	 * 
	 * @param builder
	 *            Specifies association information; is copied.
	 * @param source
	 *            Source table. Must have been previously added.
	 * @param destination
	 *            Destination table. Must have been previously added.
	 */
	public void addAssociation(AssociationBuilder builder, TableImpl source, TableImpl destination) {
		Validate.isTrue(tables.contains(source) && tables.contains(destination));
		AssociationPropertyImpl target = builder.create(source, destination);
		source.addAssociation(target);
		destination.addAssociation(target.getReverse());
	}

	/**
	 * Create a new data model from specified information. This data model is
	 * effectively immutable but potentially unsafe.
	 */
	public DataModel create() {
		return new DataModelImpl(tables);
	}
}

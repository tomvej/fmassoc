package org.tomvej.fmassoc.model.builder.simple;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
	private final Set<TableImpl> tables = new HashSet<>();
	private final Set<TableImpl> forbidden = new HashSet<>();
	private Collection<TableCache<?>> caches;

	/**
	 * Initialize builder.
	 */
	public DataModelBuilder() {
		caches = Collections.emptyList();
	}

	/**
	 * Initialize builder. Plug in caches which facilitate table search and
	 * ensure tables are unique wrt given keys.
	 * 
	 * @see TableCache
	 */
	public DataModelBuilder(TableCache<?>... cache) {
		caches = Arrays.asList(cache);
	}

	/**
	 * Add a table.
	 * 
	 * @param builder
	 *            Specifies table information; is copied.
	 * @return Resulting table or {@code null} when it cannot be added.
	 */
	public TableImpl addTable(TableBuilder builder) {
		TableImpl result = builder.create();
		if (caches.stream().anyMatch(cache -> cache.containsKey(result))) {
			return null;
		}
		tables.add(result);
		caches.forEach(cache -> cache.add(result));
		return result;
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
	 * Designate some tables as forbidden.
	 * 
	 * @param tables
	 *            Forbidden tables. Must have been previously added.
	 */
	public void addForbidden(TableImpl... tables) {
		addForbidden(Arrays.asList(tables));
	}

	/**
	 * Designate some tables as forbidden.
	 * 
	 * @param tables
	 *            Forbidden tables. Must have been previously added.
	 */
	public void addForbidden(Collection<TableImpl> tables) {
		Validate.isTrue(this.tables.containsAll(tables));
		forbidden.addAll(tables);
	}

	/**
	 * Create a new data model from specified information. This data model is
	 * effectively immutable but potentially unsafe.
	 */
	public DataModel create() {
		return new DataModelImpl(tables, forbidden);
	}
}

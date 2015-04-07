package org.tomvej.fmassoc.model.builder.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Implementation of {@link Table} interface. Is not immutable, however,
 * mutating methods are package-private.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class TableImpl extends NamedImpl implements Table {
	private final String idImplName;
	private final int number;
	private final Collection<Property> properties = new ArrayList<>();
	private final Collection<AssociationProperty> associations = new ArrayList<>();

	TableImpl(String name, String implName, String idImplName, int number) {
		super(name, implName);
		this.idImplName = Validate.notBlank(idImplName);
		this.number = number;
	}

	@Override
	public String getIDImplName() {
		return idImplName;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public Collection<AssociationProperty> getAssociations() {
		return Collections.unmodifiableCollection(associations);
	}

	@Override
	public Collection<Property> getProperties() {
		return Collections.unmodifiableCollection(properties);
	}

	void addProperty(Property target) {
		Validate.isTrue(equals(target.getParent()));
		properties.add(target);
	}

	void addAssociation(AssociationProperty target) {
		Validate.isTrue(equals(target.getDestination()) || equals(target.getSource()));
		associations.add(target);
	}
}

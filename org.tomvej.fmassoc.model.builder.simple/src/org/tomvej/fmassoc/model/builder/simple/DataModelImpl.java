package org.tomvej.fmassoc.model.builder.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.model.db.Table;

class DataModelImpl implements DataModel {
	private final Collection<Table> tables;

	DataModelImpl(Collection<? extends Table> tables) {
		this.tables = Collections.unmodifiableCollection(new ArrayList<>(tables));
	}

	@Override
	public Collection<Table> getTables() {
		return tables;
	}

	@Override
	public Collection<Table> getForbiddenTables() {
		return Collections.emptySet();
	}
}

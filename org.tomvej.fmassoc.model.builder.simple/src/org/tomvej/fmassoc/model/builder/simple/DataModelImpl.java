package org.tomvej.fmassoc.model.builder.simple;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.model.db.Table;

class DataModelImpl implements DataModel {
	private final Collection<Table> tables;
	private final Collection<Table> forbidden;

	DataModelImpl(Collection<? extends Table> tables, Collection<? extends Table> forbidden) {
		this.tables = Collections.unmodifiableCollection(new ArrayList<>(tables));
		this.forbidden = Collections.unmodifiableCollection(new ArrayList<>(forbidden));
	}

	@Override
	public Collection<Table> getTables() {
		return tables;
	}

	@Override
	public Collection<Table> getForbiddenTables() {
		return forbidden;
	}
}

package org.tomvej.fmassoc.parts.model.builders;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.model.db.Table;

class DataModelImpl implements DataModel {
	private final Map<String, Table> byName, byImplName;
	private final Map<Integer, Table> byNumber;

	DataModelImpl(Collection<? extends Table> tables) {
		byName = tables.stream().collect(Collectors.toMap(table -> table.getName(), Function.identity()));
		byImplName = tables.stream().collect(Collectors.toMap(table -> table.getImplName(), Function.identity()));
		byNumber = tables.stream().collect(Collectors.toMap(table -> table.getNumber(), Function.identity()));
	}

	@Override
	public Collection<Table> getTables() {
		return byName.values();
	}

	@Override
	public Table getTableByImplName(String name) {
		return byImplName.get(name);
	}

	@Override
	public Table getTableByName(String name) {
		return byName.get(name);
	}

	@Override
	public Table getTableByNumber(int number) {
		return byNumber.get(Integer.valueOf(number));
	}

	@Override
	public Collection<String> getTableImplNames() {
		return byImplName.keySet();
	}

	@Override
	public Collection<String> getTableNames() {
		return byName.keySet();
	}
}

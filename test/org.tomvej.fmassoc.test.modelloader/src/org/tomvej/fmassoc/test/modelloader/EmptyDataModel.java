package org.tomvej.fmassoc.test.modelloader;

import java.util.Collection;
import java.util.Collections;

import org.tomvej.fmassoc.model.db.DataModel;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Contains no tables.
 * 
 * @author Tomáš Vejpustek
 */
public class EmptyDataModel implements DataModel {

	@Override
	public Collection<Table> getForbiddenTables() {
		return Collections.emptyList();
	}

	@Override
	public Collection<Table> getTables() {
		return Collections.emptyList();
	}
}

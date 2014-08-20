package org.tomvej.fmassoc.parts.model.builders;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.apache.commons.lang3.Validate;

/**
 * Cache for tables which enables search for table wrt given key. Also ensures
 * that tables within model are unique wrt the key.
 * 
 * @author Tomáš Vejpustek
 *
 * @param <T>
 *            Key type.
 */
public class TableCache<T> {
	private final Function<TableImpl, T> keyFunction;
	private final Map<T, TableImpl> map = new HashMap<>();

	/**
	 * Specify how keys are is obtained from tables.
	 */
	public TableCache(Function<TableImpl, T> keyFunction) {
		this.keyFunction = Validate.notNull(keyFunction);
	}

	/**
	 * Get a table for a given key value.
	 */
	public TableImpl get(T key) {
		return map.get(key);
	}

	void add(TableImpl table) {
		map.put(keyFunction.apply(table), table);
	}

	boolean containsKey(TableImpl table) {
		return map.containsKey(keyFunction.apply(table));
	}
}

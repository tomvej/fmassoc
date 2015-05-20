package org.tomvej.fmassoc.parts.sql.tree.transform;

/**
 * Options available for tree sql formatter. Each one is a simple boolean
 * switch.
 * 
 * @author Tomáš Vejpustek
 */
public enum Option {
	/** Print ID_OBJECTs */
	OIDS("Print ID_OBJECTs"),
	/** Print associations (except those in path) */
	ASSOC("Print associations"),
	/** Print version properties (ID_VERSION, ...) */
	VERSION("Print version properties"),
	/** Print properties */
	PROPERTY("Print properties"),
	/** Abbreviate table names (e.g. T259) */
	ABBREV("Abbreviate table names"),
	/** Prefix column names with table names */
	PREFIX_COL("Prefix column names with table names"),
	/** Use left join instead of inner join */
	LEFT_JOIN("Use left join");


	private final String msg;

	private Option(String message) {
		msg = message;
	}

	/**
	 * Returns human-readable option description. To be used as button labels.
	 */
	public String getMessage() {
		return msg;
	}

	/**
	 * Returns tag for this option which is used to persist user selection.
	 */
	public String getTag() {
		return "OPTION_" + name();
	}
}

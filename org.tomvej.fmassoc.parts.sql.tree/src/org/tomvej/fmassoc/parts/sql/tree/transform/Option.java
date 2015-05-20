package org.tomvej.fmassoc.parts.sql.tree.transform;

public enum Option {
	OIDS("Print ID_OBJECTs"),
	ASSOC("Print associations"),
	VERSION("Print version properties"),
	PROPERTY("Print properties"),
	ABBREV("Abbreviate table names"),
	PREFIX_COL("Prefix column names with table names"),
	LEFT_JOIN("Use left join");


	private final String msg;

	private Option(String message) {
		msg = message;
	}

	public String getMessage() {
		return msg;
	}

	public String getTag() {
		return "OPTION_" + name();
	}
}

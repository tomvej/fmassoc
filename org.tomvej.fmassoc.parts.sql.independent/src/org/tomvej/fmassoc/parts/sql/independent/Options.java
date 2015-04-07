package org.tomvej.fmassoc.parts.sql.independent;

/**
 * Options available for this formatter. All are boolean.
 * 
 * @author Tomáš Vejpustek
 */
public enum Options {
	/** Print id properties columns */
	PRINT_OIDS("Print id propertiess", true),
	/** Print association properties columns */
	PRINT_ASSOC("Print associations", true),
	/** Abbreviate table names to T + number */
	ABBREV_TABLES("Abbreviate table names (i.e. T195)"),
	/** Print only columns of source and destination tables */
	SRC_DST_ONLY("Print only source and destination"),
	/** Prefix column names with table names */
	PREFIX_COLUMNS("Prefix column names with table names");


	private final String msg;
	private final boolean def;


	private Options(String message, boolean selectedByDefault) {
		msg = message;
		def = selectedByDefault;
	}

	private Options(String message) {
		this(message, false);
	}

	/**
	 * Return human-readable name for this option.
	 */
	public String getMessage() {
		return msg;
	}

	/**
	 * Return whether this option should be selected by default.
	 */
	public boolean isDefaultSelected() {
		return def;
	}

	/**
	 * Return property key for this option preference.
	 */
	public String getPropertyKey() {
		return toString();
	}

}

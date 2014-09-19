package org.tomvej.fmassoc.parts.paths.multisort;

import java.util.Collection;
import java.util.List;

import org.eclipse.swt.widgets.TableColumn;
import org.tomvej.fmassoc.core.tables.SortEntry;

/**
 * Event constants for path table multisort.
 * 
 * @author Tomáš Vejpustek
 */
public interface MultisortTopic {
	/** Overall name of this topic. Do not use as a message */
	public static final String TOPIC = "TOPIC_PATH_TABLE_MULTISORT";
	/**
	 * Columns of path table have changed. Sends a {@link Collection} of
	 * {@link TableColumn}.
	 */
	public static final String COLUMNS = TOPIC + "/COLUMNS_CHANGED";

	/**
	 * The user has chosen a multisort. Sends a {@link List} of
	 * {@link SortEntry}.
	 */
	public static final String MULTISORT = TOPIC + "/SORT_SELECTED";

	/**
	 * The user has chosen a single sort by selecting a column header.
	 * Sends a {@link TableColumn} -- to keep it simple.
	 */
	public static final String SINGLESORT = TOPIC + "/SORT_REMOVED";
}

package org.tomvej.fmassoc.parts.paths.preference;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.tomvej.fmassoc.core.properties.PathPropertyEntry;

/**
 * Event constants for preference change notification.
 * 
 * @author Tomáš Vejpustek
 */
public interface PathTablePreferenceTopic {
	/** Topic general name -- do not use as message. */
	public static final String TOPIC = "TOPIC_PATH_TABLE_PREFERENCE";
	/** Path label provider changed. Sends a {@link ColumnLabelProvider}. */
	public static final String PROVIDER_CHANGE = TOPIC + "/PROVIDER_CHANGE";
	/** Column has been added. Sends a {@link PathPropertyEntry}. */
	public static final String COLUMN_ADDED = TOPIC + "/COLUMN_ADDED";
	/** Column has been removed. Sends a {@link PathPropertyEntry} */
	public static final String COLUMN_REMOVED = TOPIC + "/COLUMN_REMOVED";
}

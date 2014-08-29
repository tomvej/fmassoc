package org.tomvej.fmassoc.parts.paths;

import org.eclipse.jface.viewers.ColumnLabelProvider;

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
}

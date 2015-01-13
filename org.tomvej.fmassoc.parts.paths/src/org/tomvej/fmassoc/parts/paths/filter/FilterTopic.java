package org.tomvej.fmassoc.parts.paths.filter;

import java.util.Collection;

import org.tomvej.fmassoc.model.property.PathProperty;

/**
 * Event constants for path table filtering.
 * 
 * @author Tomáš Vejpustek
 */
public interface FilterTopic {
	/**
	 * Overall name for this topic. Do not use as a message.
	 */
	public static final String TOPIC = "TOPIC_PATH_TABLE_FILTER";

	/**
	 * Columns of path table have changed. Sends a {@link Collection} of
	 * {@link PathProperty}.
	 */
	public static final String COLUMNS = TOPIC + "/COLUMNS_CHANGED";
}

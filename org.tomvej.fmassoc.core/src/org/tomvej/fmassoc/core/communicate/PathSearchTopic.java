package org.tomvej.fmassoc.core.communicate;

import org.tomvej.fmassoc.core.search.SearchInput;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Event constants related to the path search.
 * 
 * @author Tomáš Vejpustek
 */
public interface PathSearchTopic {
	/** Topic general name. Do not use as a message. */
	public static final String TOPIC = "TOPIC_PATH_SEARCH";
	/** Path has been found. Sends a {@link Path}. */
	public static final String PUBLISH = TOPIC + "/PUBLISH";
	/** Search has been started. Sends a {@link SearchInput} */
	public static final String START = TOPIC + "/START";
}

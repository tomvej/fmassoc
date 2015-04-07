package org.tomvej.fmassoc.core.search;

import org.eclipse.core.runtime.IStatus;
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
	/** All paths have been found. Sends an {@link IStatus}. */
	public static final String FINISH = TOPIC + "/FINISH";
	/**
	 * Search has been cancelled by the user or by reaching the maximum amount
	 * of paths. Sends an {@link IStatus}.
	 */
	public static final String CANCEL = TOPIC + "/CANCEL";
}

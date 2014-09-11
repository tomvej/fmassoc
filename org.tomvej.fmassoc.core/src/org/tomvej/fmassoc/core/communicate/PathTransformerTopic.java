package org.tomvej.fmassoc.core.communicate;

import org.eclipse.e4.ui.model.application.ui.basic.MPart;

/**
 * Events constants related to path transformation.
 * 
 * @author Tomáš Vejpustek
 */
public interface PathTransformerTopic {
	/** Topic general name. Do not use as a message */
	public static final String TOPIC = "TOPIC_PATH_TRANSFORM";
	/**
	 * Part providing path transformation has been selected. Sends the
	 * {@link MPart} in question.
	 */
	public static final String SELECT = TOPIC + "/SELECTED";
}

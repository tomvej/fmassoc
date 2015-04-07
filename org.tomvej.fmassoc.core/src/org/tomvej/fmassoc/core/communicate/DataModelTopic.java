package org.tomvej.fmassoc.core.communicate;

import org.tomvej.fmassoc.model.db.DataModel;


/**
 * Event constants related to data model. All messages post instance of
 * {@link DataModel} (if they do).
 * 
 * @author Tomáš Vejpustek
 */
public interface DataModelTopic {
	/** Topic general name. Do not use as message. */
	public static final String TOPIC = "TOPIC_DATAMODEL";
	/** All topics for data model. */
	public static final String ALL = TOPIC + "/*";
	/** New model is loading. Sends {@link String} -- model label. */
	public static final String MODEL_LOADING = TOPIC + "/MODEL_LOADING";
	/** Selected data model has changed. Sends {@link DataModel}. */
	public static final String MODEL_CHANGED = TOPIC + "/MODEL_CHANGED";
}

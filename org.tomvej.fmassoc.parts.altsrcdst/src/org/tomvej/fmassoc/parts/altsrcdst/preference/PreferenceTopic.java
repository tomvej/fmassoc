package org.tomvej.fmassoc.parts.altsrcdst.preference;

import java.util.function.Function;

import org.tomvej.fmassoc.model.db.Table;

/**
 * Event constants for source and destination chooser preference.
 * 
 * @author Tomáš Vejpustek
 */
public interface PreferenceTopic {
	/** Topic general name. Do not use as message */
	static final String TOPIC = "TOPIC_SOURCE_DESTINATION_PREFERENCE";

	/**
	 * Table display property changed. Sends {@link Function} from {@link Table}
	 * to {@link String}.
	 */
	static final String DISPLAY_PROPERTY_CHANGE = TOPIC + "/DISPLAY_PROPERTY";

}

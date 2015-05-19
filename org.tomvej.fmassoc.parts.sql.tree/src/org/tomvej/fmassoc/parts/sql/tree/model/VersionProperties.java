package org.tomvej.fmassoc.parts.sql.tree.model;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.tomvej.fmassoc.model.db.Property;

enum VersionProperties {
	ID_VERSION,
	ID_BRANCH,
	ID_PREV_VERSION1,
	ID_PREV_VERSION2,
	FG_OBJ_DELETED,
	ID_USER,
	TS_USER,
	TS_SERVER,
	ID_MSG_RCVD,
	IND_SYNC,
	IND_COMMS_PRIORITY;

	private static Set<String> properties = Arrays.stream(values()).map(VersionProperties::name).collect(Collectors.toSet());

	static boolean isVersionProperty(Property target) {
		return properties.contains(target.getImplName().toUpperCase());
	}
}

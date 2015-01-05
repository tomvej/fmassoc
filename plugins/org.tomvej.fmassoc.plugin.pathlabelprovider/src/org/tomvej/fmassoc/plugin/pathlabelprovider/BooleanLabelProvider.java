package org.tomvej.fmassoc.plugin.pathlabelprovider;

import org.tomvej.fmassoc.parts.paths.labelprovider.CustomColumnLabelProvider;

/**
 * Label provider for boolean using "Yes" and "No".
 * 
 * @author Tomáš Vejpustek
 */
public class BooleanLabelProvider implements CustomColumnLabelProvider<Boolean> {

	@Override
	public String getText(Boolean element) {
		return element ? "Yes" : "No";
	}
}

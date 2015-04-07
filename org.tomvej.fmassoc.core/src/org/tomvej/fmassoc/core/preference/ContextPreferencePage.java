package org.tomvej.fmassoc.core.preference;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.preference.IPreferencePage;

/**
 * Preference page which has access to eclipse context.
 * 
 * @author Tomáš Vejpustek
 */
public interface ContextPreferencePage extends IPreferencePage {

	/**
	 * Sets application eclipse context. Guaranteed to execute after
	 * constructor.
	 */
	void init(IEclipseContext context);
}

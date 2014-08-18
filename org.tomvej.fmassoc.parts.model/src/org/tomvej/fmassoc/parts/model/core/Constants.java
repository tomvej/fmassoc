package org.tomvej.fmassoc.parts.model.core;


/**
 * Constants used by the org.tomvej.fmassoc.parts.model plugin.
 * 
 * @author Tomáš Vejpustek
 *
 */
public interface Constants {
	/**
	 * Id of this plugin.
	 */
	public static final String PLUGIN_ID = "org.tomvej.fmassoc.parts.model";

	/**
	 * Constant for storing list of model loaders in the eclipse context.
	 * Relates to {@code List<ModelLoaderEntry>}.
	 */
	public static final String MODEL_LOADER_REGISTRY = PLUGIN_ID + ".ModelLoaderRegistry";
}

package org.tomvej.fmassoc.plugin.mobilemodelloader.xml;

/**
 * Data type position in type hierarchy.
 * 
 * @author Tomáš Vejpustek
 */
public enum DataTypeHierarchy {
	/** Root type without subtypes */
	ROOT_WITHOUT_SUBTYPES(true),
	/** Root type */
	ROOT_WITH_SUBTYPES(true),
	/** Subtype */
	FINAL_SUBTYPE(false);

	private final boolean root;

	private DataTypeHierarchy(boolean root) {
		this.root = root;
	}

	/**
	 * Return whether it is a root type.
	 */
	public boolean isRoot() {
		return root;
	}
}

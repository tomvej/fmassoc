package org.tomvej.fmassoc.transform.sql.handles;

/**
 * SQL representation of model object (table or its column).
 * Comprises two strings:
 * <ul>
 * <li>declaration -- long name which is used when the object is first
 * mentioned,
 * <li>reference -- short name which is used everywhere else.
 * </ul>
 * 
 * 
 * @author Tomáš Vejpustek
 */
public interface Handle {
	/**
	 * Return object declaration -- long name used on its first mention.
	 */
	String getDeclaration();

	/**
	 * Return object reference -- short name used to refer to it.
	 */
	String getReference();
}

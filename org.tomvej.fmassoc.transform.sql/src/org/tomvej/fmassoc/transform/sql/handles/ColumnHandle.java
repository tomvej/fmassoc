package org.tomvej.fmassoc.transform.sql.handles;

/**
 * SQL representation of a table column (property).
 * 
 * <p>
 * Its declaration is its full name (including name of the parent table), its
 * reference is its alias (optionally -- it can be its full name). When an alias
 * is specified, it must be defined in declaration part.
 * </p>
 * 
 * <p>
 * Query-wise declaration is used in SELECT expression and reference everywhere
 * else.
 * </p>
 * 
 * Examples (declaration -> reference):
 * <ul>
 * <li>WORK_REQUEST.ID_OBJECT -> WORK_REQUEST.ID_OBJECT</li>
 * <li>WORK_REQUEST.ID_OBJECT AS ido_wr -> ido_wr</li>
 * </ul>
 * 
 * @author Tomáš Vejpustek
 *
 * @see TableHandle
 */
public interface ColumnHandle extends Handle {

	/**
	 * Specifies whether this column should be displayed in case when only
	 * specific columns of its parent table should be displayed.
	 * 
	 * @see TableHandle#isDisplayed()
	 */
	boolean isDisplayed();

}

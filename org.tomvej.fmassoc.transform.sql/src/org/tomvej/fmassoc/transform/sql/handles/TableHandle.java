package org.tomvej.fmassoc.transform.sql.handles;

/**
 * SQL representation of database table.
 * 
 * <p>
 * Its reference may be an alias in which case declaration must include alias
 * definition. Query-wise declaration is used in FROM or JOIN expression,
 * reference most notably as a part of column handles.
 * </p>
 * 
 * Examples (declaration -> reference):
 * <ul>
 * <li>WORK_REQUEST -> WORK_REQUEST</li>
 * <li>WORK_REQUEST as wr -> wr</li>
 * </ul>
 * 
 * @author Tomáš Vejpustek
 * 
 * @see ColumnHandle
 *
 */
public interface TableHandle extends Handle {

	/**
	 * Specifies how this table should be displayed in the SELECT expression.
	 */
	DisplayState isDisplayed();

	/**
	 * Manners in which a table may be displayed in SELECT expression.
	 * 
	 * @author Tomáš Vejpustek
	 */
	public enum DisplayState {
		/** All columns are displayed */
		ALL,
		/**
		 * Some columns are displayed -- specified by
		 * {@link ColumnHandle#isDisplayed()}
		 */
		SELECTED,
		/** No columns are displayed */
		NONE;
	}
}

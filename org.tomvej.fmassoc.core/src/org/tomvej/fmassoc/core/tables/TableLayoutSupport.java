package org.tomvej.fmassoc.core.tables;

import java.util.Arrays;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Class facilitating setup of table layout.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class TableLayoutSupport {
	private final TableColumnLayout layout;

	/**
	 * Create a table layout for a table.
	 * 
	 * @param table
	 *            Target table. Must be the only control in its parent
	 *            composite.
	 */
	public TableLayoutSupport(Table table) {
		Validate.notNull(table);
		layout = new TableColumnLayout();
		table.getParent().setLayout(layout);
	}

	/**
	 * Create a table layout for a table viewer.
	 * 
	 * @param viewer
	 *            Target table viewer. Must be the only control in its parent
	 *            composite
	 */
	public TableLayoutSupport(TableViewer viewer) {
		this(viewer.getTable());
	}

	/**
	 * Add a column to the layout.
	 * 
	 * @param column
	 *            Target column.
	 * @param weight
	 *            Weight of column -- how much space it takes wrt other columns.
	 * @param resizable
	 *            Whether the column can be resized.
	 * @return Reference to this object.
	 */
	public TableLayoutSupport setupColumn(TableColumn column, int weight, boolean resizable) {
		layout.setColumnData(column, new ColumnWeightData(weight, resizable));
		return this;
	}

	/**
	 * Add a column to the layout.
	 * 
	 * @param column
	 *            Target column.
	 * @param weight
	 *            Weight of column -- how much space it takes wrt other columns.
	 * @param resizable
	 *            Whether the column can be resized.
	 * @return Reference to this object.
	 */
	public TableLayoutSupport setupColumn(TableViewerColumn column, int weight, boolean resizable) {
		setupColumn(column.getColumn(), weight, resizable);
		return this;
	}

	/**
	 * Create a new table
	 * 
	 * @param table
	 *            Target table.
	 * @param weight
	 *            Weight of all columns.
	 * @param resizable
	 *            Whether all columns should be resizable.
	 * @param columns
	 *            List of columns.
	 * @return Table layout support with given parameters.
	 */
	public static TableLayoutSupport create(Table table, int weight, boolean resizable, TableColumn... columns) {
		TableLayoutSupport result = new TableLayoutSupport(table);
		Arrays.asList(columns).forEach(column -> result.setupColumn(column, weight, resizable));
		return result;
	}

	/**
	 * Create a new table
	 * 
	 * @param viewer
	 *            Target table.
	 * @param weight
	 *            Weight of all columns.
	 * @param resizable
	 *            Whether all columns should be resizable.
	 * @param columns
	 *            List of columns.
	 * @return Table layout support with given parameters.
	 */
	public static TableLayoutSupport create(TableViewer viewer, int weight, boolean resizable, TableViewerColumn... columns) {
		TableLayoutSupport result = new TableLayoutSupport(viewer);
		Arrays.asList(columns).forEach(col -> result.setupColumn(col, weight, resizable));
		return result;
	}

	/**
	 * Create table viewer so that it is wrapped in a separate component.
	 * 
	 * @param parent
	 *            Parent component.
	 * @param style
	 *            Table viewer style.
	 * @param layoutData
	 *            Layout data wrt parent component.
	 * @return New table viewer.
	 */
	public static TableViewer createTableViewer(Composite parent, int style, Object layoutData) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(layoutData);
		TableViewer result = new TableViewer(tableComposite, style);
		return result;
	}

	/***
	 * Create checkbox table viewer so that it is wrapped in a separate
	 * component.
	 * 
	 * 
	 * @param parent
	 *            Parent component.
	 * @param style
	 *            Table viewer style.
	 * @param layoutData
	 *            Layout data wrt parent component.
	 * @return New table viewer.
	 */
	public static CheckboxTableViewer createCheckboxTableViewer(Composite parent, int style, Object layoutData) {
		Composite tableComposite = new Composite(parent, SWT.NONE);
		tableComposite.setLayoutData(layoutData);
		return CheckboxTableViewer.newCheckList(tableComposite, style);
	}

}

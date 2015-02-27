package org.tomvej.fmassoc.parts.altsrcdst;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.core.tables.ColumnSortSupport;
import org.tomvej.fmassoc.core.tables.TableLayoutSupport;
import org.tomvej.fmassoc.core.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.core.wrappers.ViewerFilterWrapper;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Pop-up table chooser table list component.
 * 
 * @author Tomáš Vejpustek
 */
public class TablePopupTable {

	private final TableViewer tables;
	private Collection<Object> tableFilter = Collections.emptySet();
	private Pattern namePattern = Pattern.compile(""),
			implNamePattern = namePattern;

	/**
	 * Specify parent pop-up shell.
	 */
	public TablePopupTable(Shell parent, Consumer<Table> selectionListener) {
		tables = TableLayoutSupport.createTableViewer(parent,
				SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER,
				GridDataFactory.fillDefaults().grab(true, true).create());
		tables.getTable().setHeaderVisible(true);
		tables.getTable().setLinesVisible(true);
		tables.setContentProvider(ArrayContentProvider.getInstance());

		/* name column */
		TableViewerColumn nameClmn = new TableViewerColumn(tables, SWT.NONE);
		nameClmn.getColumn().setText("Name");
		nameClmn.setLabelProvider(new TextColumnLabelProvider<Table>(table -> table.getName()));

		/* implementation name column */
		TableViewerColumn implNameClmn = new TableViewerColumn(tables, SWT.NONE);
		implNameClmn.getColumn().setText("Implementation name");
		implNameClmn.setLabelProvider(new TextColumnLabelProvider<Table>(table -> table.getImplName()));

		new ColumnSortSupport(tables).sortByFirstColumn();
		TableLayoutSupport.create(tables, 1, true, nameClmn, implNameClmn);

		/* filters */
		tables.addFilter(new ViewerFilterWrapper<Table>(table -> !tableFilter.contains(table)));
		tables.addFilter(new ViewerFilterWrapper<Table>(
				table -> (namePattern.matcher(table.getName()).find())
						|| implNamePattern.matcher(table.getImplName()).find()));

		tables.addSelectionChangedListener(e -> selectionListener.accept(getSelecedTable()));
	}

	/**
	 * Set tables displayed in this pop-up window.
	 */
	public void setTables(Collection<Table> tables) {
		this.tables.setInput(Validate.notNull(tables));
	}

	/**
	 * Set collection of tables not displayed in this pop-up window.
	 */
	public void setFilter(Collection<Object> tables) {
		if (tables == null) {
			tableFilter = Collections.emptySet();
		} else {
			tableFilter = tables;
		}
		this.tables.refresh();
	}

	/**
	 * Set regular expression filter on shown tables.
	 */
	public void setFilter(String text) {
		if (text == null) {
			text = "";
		}
		namePattern = compilePattern(text.replace(' ', '_'));
		implNamePattern = compilePattern(StringUtils.remove(text, ' '));
		tables.refresh();
	}

	/**
	 * Return selected table.
	 */
	public Table getSelecedTable() {
		return (Table) ((IStructuredSelection) tables.getSelection()).getFirstElement();
	}


	private static Pattern compilePattern(String pattern) {
		try {
			return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		} catch (PatternSyntaxException pse) {
			return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
		}
	}

	/**
	 * Move selection. If no table is selected, start from the beginning or end.
	 * 
	 * @param increment
	 *            Number of positions to move selection.
	 */
	public void move(int increment) {
		if (increment == 0) {
			return;
		}
		int itemCount = tables.getTable().getItemCount();

		int index;
		if (tables.getTable().getSelectionCount() > 0) {
			index = tables.getTable().getSelectionIndex();
		} else {
			if (increment > 0) {
				index = -1;
			} else {
				index = itemCount;
			}
		}
		index += increment;
		if (index < 0) {
			index = 0;
		} else if (index >= itemCount) {
			index = itemCount - 1;
		}
		tables.getTable().setSelection(index);
	}

	/**
	 * Return bounds of table component.
	 */
	public Rectangle getBounds() {
		return tables.getTable().getParent().getBounds();
	}

	/**
	 * Select no table.
	 */
	public void clearSelection() {
		tables.getTable().setSelection(new int[0]);
	}

}

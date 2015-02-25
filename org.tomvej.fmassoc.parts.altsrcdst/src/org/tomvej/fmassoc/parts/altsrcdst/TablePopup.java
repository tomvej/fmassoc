package org.tomvej.fmassoc.parts.altsrcdst;

import java.util.Collection;
import java.util.Collections;
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
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.tomvej.fmassoc.core.tables.ColumnSortSupport;
import org.tomvej.fmassoc.core.tables.TableLayoutSupport;
import org.tomvej.fmassoc.core.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.core.wrappers.ViewerFilterWrapper;
import org.tomvej.fmassoc.model.db.Table;

public class TablePopup {
	private static int SHELL_STYLE = SWT.MODELESS | SWT.NO_TRIM;

	private final TableViewer tables;
	private Collection<Object> tableFilter = Collections.emptySet();
	private Pattern namePattern = Pattern.compile(""),
			implNamePattern = namePattern;
	private Runnable listener;

	/**
	 * Specify parent shell.
	 */
	public TablePopup(Shell parent) {
		tables = TableLayoutSupport.createTableViewer(new Shell(parent, SHELL_STYLE),
				SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER,
				GridDataFactory.fillDefaults().grab(true, true).create());
		setShellLayout();

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

		tables.addSelectionChangedListener(e -> listener.run());
	}

	private void setShellLayout() {
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		getShell().setLayout(layout);
	}

	private Shell getShell() {
		return getControl().getShell();
	}

	private Control getControl() {
		return tables.getTable();
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

	private static Pattern compilePattern(String pattern) {
		try {
			return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
		} catch (PatternSyntaxException pse) {
			return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
		}
	}

	public void show(Control c, Runnable selectionListener) {
		Point size = c.getSize();
		getShell().setLocation(c.toDisplay(0, size.y));
		getShell().setVisible(true);

		listener = Validate.notNull(selectionListener);
	}

	public void hide() {
		getShell().setVisible(false);
		listener = null;
	}

	public Table getSelection() {
		return (Table) ((IStructuredSelection) tables.getSelection()).getFirstElement();
	}

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

	public boolean isVisible() {
		return getShell().isVisible();
	}

}

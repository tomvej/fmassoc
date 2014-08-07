package org.tomvej.fmassoc.core.widgets.tablechooser;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.core.tables.ColumnSortSupport;
import org.tomvej.fmassoc.core.tables.TableLayoutSupport;
import org.tomvej.fmassoc.core.wrappers.TextColumnLabelProvider;
import org.tomvej.fmassoc.core.wrappers.ViewerFilterWrapper;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Widget which allows user to select among a list of tables. Entails a regular
 * expression filter.
 * 
 * @author Tomáš Vejpustek
 */
public class TableChooser extends Composite {
	private final Text search;
	private final TableViewer tables;
	private Consumer<Table> listener;
	private Pattern pattern = Pattern.compile("");
	private Set<Table> filter = Collections.emptySet();

	/**
	 * Create this widget.
	 */
	public TableChooser(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new GridLayout(1, false));
		GridDataFactory layout = GridDataFactory.fillDefaults();

		search = new Text(this, SWT.SEARCH | SWT.CANCEL);
		search.setLayoutData(layout.grab(true, false).create());
		search.setMessage("Table filter");
		search.addModifyListener(this::textModified);

		// so that column layout can be used
		Composite tableComposite = new Composite(this, SWT.NONE);
		tableComposite.setLayoutData(layout.grab(true, true).create());

		tables = new TableViewer(tableComposite, SWT.SINGLE | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		tables.getTable().setHeaderVisible(true);
		tables.getTable().setLinesVisible(true);
		tables.setContentProvider(ArrayContentProvider.getInstance());
		tables.addFilter(new ViewerFilterWrapper<Table>(
				table -> pattern.matcher(table.getName()).find() || pattern.matcher(table.getImplName()).find()));
		tables.addFilter(new ViewerFilterWrapper<Table>(table -> !filter.contains(table)));
		tables.addSelectionChangedListener(event -> {
			if (listener != null) {
				listener.accept(getSelection());
			}
		});

		/* "name" column */
		TableViewerColumn nameClmn = new TableViewerColumn(tables, SWT.NONE);
		nameClmn.getColumn().setText("Name");
		nameClmn.setLabelProvider(new TextColumnLabelProvider<Table>(table -> table.getName()));

		/* "implementation name" column */
		TableViewerColumn implNameClmn = new TableViewerColumn(tables, SWT.NONE);
		implNameClmn.getColumn().setText("Implementation name");
		implNameClmn.setLabelProvider(new TextColumnLabelProvider<Table>(table -> table.getImplName()));

		new ColumnSortSupport(tables).sortByFirstColumn();
		TableLayoutSupport.create(tables, 1, true, nameClmn, implNameClmn);
	}

	/**
	 * Set tables to be chosen from.
	 */
	public void setTables(Collection<Table> tables) {
		this.tables.setInput(tables);
	}

	/**
	 * Filter a some tables out of the table list.
	 */
	public void setFilter(Set<Table> tables) {
		if (tables == null) {
			filter = Collections.emptySet();
		} else {
			filter = tables;
		}
		this.tables.refresh();
	}

	/**
	 * Attach a listener which registers selected table changes.
	 * 
	 * @param listener
	 *            A listener or {@code null} to disable listening.
	 */
	public void setTableListener(Consumer<Table> listener) {
		this.listener = listener;
	}

	/**
	 * Return currently selected table (or {@code null}).
	 */
	public Table getSelection() {
		return (Table) ((IStructuredSelection) tables.getSelection()).getFirstElement();
	}

	private void textModified(ModifyEvent event) {
		String text = search.getText().replace(' ', '_');
		try {
			pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE);
		} catch (PatternSyntaxException pse) {
			pattern = Pattern.compile(text, Pattern.CASE_INSENSITIVE | Pattern.LITERAL);
		}
		tables.refresh();
	}

	@Override
	public boolean setFocus() {
		return search.setFocus();
	}
}
package org.tomvej.fmassoc.parts.altsrcdst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.altsrcdst.popup.TablePopup;
import org.tomvej.fmassoc.swt.tables.ColumnSortSupport;
import org.tomvej.fmassoc.swt.tables.TableLayoutSupport;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.swt.wrappers.TextColumnLabelProvider;

/**
 * Component used to select forbidden tables.
 * 
 * @author Tomáš Vejpustek
 */
public class ForbiddenChooser extends Group {
	@Override
	protected void checkSubclass() {} // can subclass

	private final CheckboxTableViewer table;
	private final IObservableList forbidden = Properties.selfList(Table.class).observe(new ArrayList<>());
	private final Text input;
	private final TablePopup popup;
	private final Button addBtn;

	private Collection<Table> defaultForbidden = Collections.emptySet();
	private Consumer<Set<Table>> tableListener;

	private Table inputTable;

	/**
	 * Specify parent component.
	 */
	public ForbiddenChooser(Composite parent) {
		super(parent, SWT.SHADOW_ETCHED_IN);
		setText("Forbidden Tables");
		setLayout(new GridLayout(3, false));

		table = TableLayoutSupport.createCheckboxTableViewer(this,
				SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.BORDER | SWT.MULTI,
				GridDataFactory.fillDefaults().grab(true, true).span(3, 1).create());
		table.getTable().setHeaderVisible(true);
		table.getTable().setLinesVisible(true);
		createColumns();

		table.setContentProvider(new ObservableListContentProvider());
		table.setInput(forbidden);
		table.addCheckStateListener(e -> fireChanges());
		table.addSelectionChangedListener(this::bypassSelection);

		input = new Text(this, SWT.BORDER);
		input.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());
		popup = new TablePopup(getShell(), new Point(250, 350));
		popup.attach(input, () -> inputTable, this::tableChosen);

		addBtn = createButton("Add");
		addBtn.addSelectionListener(new SelectionWrapper(e -> addTable()));
		Button rmBtn = createButton("Remove");
		rmBtn.addSelectionListener(new SelectionWrapper(e -> removeTables()));

		table.addSelectionChangedListener(e -> rmBtn.setEnabled(!table.getSelection().isEmpty()));
	}

	private void createColumns() {
		/* name */
		TableViewerColumn nameColumn = new TableViewerColumn(table, SWT.LEFT);
		nameColumn.getColumn().setText("Name");
		nameColumn.setLabelProvider(new LabelProvider(t -> t.getName()));

		/* implementation name */
		TableViewerColumn implNameColumn = new TableViewerColumn(table, SWT.LEFT);
		implNameColumn.getColumn().setText("Implementation Name");
		implNameColumn.setLabelProvider(new LabelProvider(t -> t.getImplName()));

		new ColumnSortSupport(table);

		/* check */
		TableViewerColumn checkColumn = new TableViewerColumn(table, SWT.CENTER, 0);
		checkColumn.setLabelProvider(new TextColumnLabelProvider<Table>(t -> ""));
		checkColumn.getColumn().setResizable(false);

		TableLayoutSupport.create(table, 1, true, nameColumn, implNameColumn)
				.setupWidthColumn(checkColumn, 30, false, false);
	}

	private Button createButton(String label) {
		Button result = new Button(this, SWT.PUSH);
		result.setText(label);
		result.setLayoutData(GridDataFactory.fillDefaults().create());
		result.setEnabled(false);
		return result;
	}

	private void bypassSelection(SelectionChangedEvent e) {
		List<Table> selection = getSelection();
		if (selection.stream().anyMatch(t -> defaultForbidden.contains(t))) {
			table.removeSelectionChangedListener(this::bypassSelection);
			table.setSelection(new StructuredSelection(
					selection.stream().filter(t -> !defaultForbidden.contains(t)).collect(Collectors.toList())));
			table.addSelectionChangedListener(this::bypassSelection);
		}
	}

	@SuppressWarnings("unchecked")
	private List<Table> getSelection() {
		return ((IStructuredSelection) table.getSelection()).toList();
	}

	private void tableChosen(Table table) {
		inputTable = table;
		input.setText(table != null ? table.getName() : "");
		addBtn.setEnabled(table != null);
	}

	@SuppressWarnings("unchecked")
	private void addTable() {
		forbidden.add(inputTable);
		tableChosen(null);
		popup.setFilter(forbidden);
	}

	private void removeTables() {
		List<Table> selected = getSelection();
		boolean changed = selected.stream().anyMatch(t -> table.getChecked(t));
		forbidden.removeAll(selected);
		if (changed) {
			fireChanges();
		}
	}

	/**
	 * Specify tables to choose from.
	 * 
	 * @param tables
	 *            Tables to choose from.
	 * @param forbidden
	 *            Tables which are forbidden by default.
	 */
	public void setTables(Collection<Table> tables, Collection<Table> forbidden) {
		defaultForbidden = forbidden != null ? forbidden : Collections.emptySet();
		this.forbidden.clear();
		this.forbidden.addAll(defaultForbidden);
		table.setCheckedElements(defaultForbidden.toArray());

		popup.setTables(tables);
		popup.setFilter(Collections.unmodifiableCollection(defaultForbidden));
	}

	/**
	 * Specify listener which is notified when forbidden tables are changed.
	 */
	public void setTableListener(Consumer<Set<Table>> listener) {
		tableListener = listener;
	}

	private void fireChanges() {
		if (tableListener != null) {
			tableListener.accept(getForbiddenTables());
		}
	}

	@SuppressWarnings("unchecked")
	private Set<Table> getForbiddenTables() {
		return (Set<Table>) forbidden.stream().filter(t -> table.getChecked(t)).collect(Collectors.toSet());
	}


	/**
	 * Changes color for default forbidden tables.
	 * 
	 * @author Tomáš Vejpustek
	 */
	private class LabelProvider extends TextColumnLabelProvider<Table> {

		private LabelProvider(Function<Table, String> provider) {
			super(provider);
		}

		@Override
		public Color getForeground(Object element) {
			if (defaultForbidden.contains(element)) {
				return getShell().getDisplay().getSystemColor(SWT.COLOR_TITLE_INACTIVE_FOREGROUND);
			}
			return super.getForeground(element);
		}

	}
}

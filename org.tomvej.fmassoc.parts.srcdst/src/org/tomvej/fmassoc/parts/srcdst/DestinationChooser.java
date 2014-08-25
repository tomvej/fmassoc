package org.tomvej.fmassoc.parts.srcdst;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.eclipse.core.databinding.observable.list.IObservableList;
import org.eclipse.core.databinding.property.Properties;
import org.eclipse.jface.databinding.viewers.ObservableListContentProvider;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.tomvej.fmassoc.core.widgets.tablechooser.TableChooser;
import org.tomvej.fmassoc.core.wrappers.SelectionWrapper;
import org.tomvej.fmassoc.core.wrappers.TextLabelProvider;
import org.tomvej.fmassoc.model.db.Table;

/**
 * 
 * Component used to choose destinations table -- either single or multiple
 * destinations.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class DestinationChooser extends Group {
	@Override
	protected void checkSubclass() {} // allow subclassing

	private final TableChooser tables;
	private final IObservableList destinations;
	private final ListViewer dstList;
	private final Button switcher;
	// note: visibility will be difficult
	private final Composite destinationComposite;
	private final GridData destinationData;
	private final Button addBtn, delBtn, upBtn, downBtn;

	private Consumer<List<Table>> listener;

	/**
	 * Specify parent composite.
	 */
	public DestinationChooser(Composite parent) {
		super(parent, SWT.SHADOW_ETCHED_OUT);
		setLayout(new GridLayout());
		GridDataFactory layout = GridDataFactory.fillDefaults();

		destinationComposite = new Composite(this, SWT.NONE);
		destinationData = layout.grab(true, false).create();
		destinationComposite.setLayoutData(destinationData);

		tables = new TableChooser(this);
		tables.setLayoutData(layout.grab(true, true).create());
		tables.setTableListener(this::tableSelected);

		switcher = new Button(this, SWT.CHECK);
		switcher.setLayoutData(layout.grab(false, false).create());
		switcher.setText("Multiple successive destination tables");
		switcher.addSelectionListener(new SelectionWrapper(e -> multiSwitched()));

		destinationComposite.setLayout(new GridLayout(2, false));

		dstList = new ListViewer(destinationComposite, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		dstList.getList().setLayoutData(layout.grab(true, true).span(1, 4).create());

		dstList.setLabelProvider(new TextLabelProvider<Table>(
				table -> table.getName() + " (" + table.getImplName() + ")"));
		dstList.setContentProvider(new ObservableListContentProvider());
		destinations = Properties.selfList(Table.class).observe(new ArrayList<>());
		dstList.setInput(destinations);

		dstList.addSelectionChangedListener(e -> refreshButtons());
		destinations.addChangeListener(e -> refreshButtons());

		addBtn = createDestinationCompositeButton("Add", e -> addSelected());
		addBtn.setEnabled(false);
		delBtn = createDestinationCompositeButton("Remove", e -> removeSelected());
		upBtn = createDestinationCompositeButton("Up", e -> moveSelected(true));
		downBtn = createDestinationCompositeButton("Down", e -> moveSelected(false));

		setLayout();
		refreshButtons();
	}

	private Button createDestinationCompositeButton(String text, Consumer<SelectionEvent> action) {
		Button result = new Button(destinationComposite, SWT.PUSH);
		result.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		result.setText(text);
		result.addSelectionListener(new SelectionWrapper(action));
		return result;
	}

	private void setLayout() {
		destinationComposite.setVisible(isMulti());
		destinationData.exclude = !isMulti();
		layout();
		setText("Destination Table" + (isMulti() ? "s" : ""));
	}

	private void multiSwitched() {
		setLayout();

		/* data concerns */
		if (!isMulti()) {
			tables.setFilter(null);

			Table selected = tables.getSelection();
			if (selected != null) {
				// table in destinations cannot be selected in table chooser
				destinations.clear();
				destinations.add(selected);
			} else if (!destinations.isEmpty()) {
				destinations.clear();
			} else {
				return; // do not fire change
			}
			fireChanges();
		} else {
			refreshFilter();
		}
	}

	private void tableSelected(Table selection) {
		boolean selected = selection != null;
		addBtn.setEnabled(selected);
		if (!isMulti()) {
			if (selected || !destinations.isEmpty()) {
				destinations.clear();
				if (selected) {
					destinations.add(selection);
				}
				fireChanges();
			}
		}
	}

	private boolean isMulti() {
		return switcher.getSelection();
	}

	@SuppressWarnings("unchecked")
	private void refreshFilter() {
		tables.setFilter(destinations);
	}

	private void refreshButtons() {
		delBtn.setEnabled(!dstList.getSelection().isEmpty());
		boolean single = dstList.getList().getSelectionCount() == 1;
		int index = dstList.getList().getSelectionIndex();
		upBtn.setEnabled(single && index != 0);
		downBtn.setEnabled(single && index != destinations.size() - 1);
	}

	private void moveSelected(boolean up) {
		int index = dstList.getList().getSelectionIndex();
		int newIndex = index + (up ? -1 : +1);
		destinations.set(index, destinations.set(newIndex, destinations.get(index)));
		fireChanges();

		dstList.getList().setSelection(newIndex);
		refreshButtons();
		// without another refresh looks weird for two tables and down table
	}

	private void addSelected() {
		destinations.add(tables.getSelection());
		fireChanges();

		refreshFilter();
		addBtn.setEnabled(false);
	}

	private void removeSelected() {
		destinations.removeAll(((IStructuredSelection) dstList.getSelection()).toList());
		fireChanges();

		refreshFilter();
	}

	private void fireChanges() {
		if (listener != null) {
			listener.accept(getSelection());
		}
	}

	/**
	 * Specify a listener which is notified when selected destination tables
	 * change.
	 */
	public void setTableListener(Consumer<List<Table>> listener) {
		this.listener = listener;
	}

	/**
	 * Specify tables among which destination tables may be chosen.
	 */
	public void setTables(Collection<Table> tables) {
		this.tables.setTables(tables);
		if (destinations.isEmpty()) {
			destinations.clear();
			fireChanges();
		}
	}

	/**
	 * Retrieve current destination tables.
	 */
	@SuppressWarnings("unchecked")
	public List<Table> getSelection() {
		return Collections.unmodifiableList(destinations);
	}
}
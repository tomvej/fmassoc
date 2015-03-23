package org.tomvej.fmassoc.parts.altsrcdst.srcdst;

import java.util.function.Consumer;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.altsrcdst.popup.TablePopup;
import org.tomvej.fmassoc.swt.dnd.CompositeDnDSupport;
import org.tomvej.fmassoc.swt.wrappers.SelectionWrapper;

/**
 * Component used to choose table in Source to Destination panel.
 * 
 * @author Tomáš Vejpustek
 */
public class TableChooser extends Composite {
	private final Text input;
	private final Label knob;

	private Table table;
	private Consumer<Table> listener;

	/**
	 * Specify parent panel and pop-up used to select table.
	 */
	public TableChooser(Composite parent, TablePopup popup) {
		super(parent, SWT.BORDER);
		setLayout(new GridLayout(3, false));
		setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		knob = new Label(this, SWT.BORDER);
		knob.setLayoutData(GridDataFactory.fillDefaults().hint(15, SWT.DEFAULT).create());
		knob.setBackground(getShell().getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));

		input = new Text(this, SWT.SINGLE | SWT.BORDER);
		popup.attach(input, () -> table, this::tableSet);
		input.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		Button rmBtn = new Button(this, SWT.PUSH);
		rmBtn.setText("X");
		rmBtn.addSelectionListener(new SelectionWrapper(e -> {
			dispose();
			parent.getParent().layout();
		}));

		setTabList(new Control[] { input });
	}

	/**
	 * Plug-in drag and drop support to this component.
	 */
	public void addDnDSupport(CompositeDnDSupport dnd) {
		dnd.registerKnob(knob, this);
		dnd.registerKnob(this, this);
	}

	private void tableSet(Table table) {
		this.table = table;
		input.setText(table.getName());
		input.setSelection(input.getText().length());
		if (listener != null) {
			listener.accept(table);
		}
	}

	/**
	 * Return table selected by this component.
	 */
	public Table getTable() {
		return table;
	}

	/**
	 * Specify listener which is notified when table selection is changed.
	 */
	public void setTableListener(Consumer<Table> listener) {
		this.listener = listener;
	}

}

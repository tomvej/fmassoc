package org.tomvej.fmassoc.parts.altsrcdst.popup;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.swt.wrappers.KeyEventBlocker;
import org.tomvej.fmassoc.swt.wrappers.KeyReleasedSimpleWrapper;
import org.tomvej.fmassoc.swt.wrappers.KeyReleasedWrapper;
import org.tomvej.fmassoc.swt.wrappers.MouseUpWrapper;

/**
 * Pop-up window used to select a table. Contains a table list and a text which
 * is used to filter the table list.
 * 
 * @author Tomáš Vejpustek
 */
public class TablePopup {
	private static final int SHELL_STYLE = SWT.MODELESS | SWT.NO_TRIM;

	private final Point shellMinSize;
	private final TablePopupTable tables;
	private final Text input;

	private Consumer<Table> tableListener;
	private Text target;
	private boolean closing;

	/**
	 * Specify parent shell.
	 */
	public TablePopup(Shell parent, Point size) {
		shellMinSize = size;

		Shell popup = new Shell(parent, SHELL_STYLE);
		popup.setLayout(getShellLayout());
		popup.addShellListener(new ShellListener());
		popup.setSize(size);

		input = new Text(popup, SWT.SINGLE | SWT.BORDER);
		input.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		tables = new TablePopupTable(popup, t -> accept(null));

		/* input */
		input.addModifyListener(e -> tables.setFilter(input.getText()));
		input.addKeyListener(new KeyEventBlocker(SWT.ARROW_UP, SWT.ARROW_DOWN));
		input.addTraverseListener(this::traverse);
		input.addKeyListener(new KeyReleasedSimpleWrapper(this::keyReleased));
	}

	private Layout getShellLayout() {
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.verticalSpacing = 1;
		return layout;
	}

	private Shell getShell() {
		return input.getShell();
	}

	/**
	 * Set tables displayed in this pop-up window.
	 */
	public void setTables(Collection<Table> tables) {
		this.tables.setTables(tables != null ? tables : Collections.emptyList());
	}


	/**
	 * Set collection of tables not displayed in this pop-up window.
	 */
	public void setFilter(Collection<Object> tables) {
		this.tables.clearSelection();
		this.tables.setFilter(tables);
	}

	/**
	 * Open the pop-up window.
	 * 
	 * @param target
	 *            Overlaid text input.
	 * @param table
	 *            Table which was previously selected.
	 * @param listener
	 *            Notified when table is selected.
	 */
	private void open(Text target, Table table, Consumer<Table> listener) {
		this.target = target;
		tableListener = Validate.notNull(listener);
		tables.setNonFilteredTable(table);

		getShell().setLocation(target.getParent().toDisplay(target.getLocation()));
		getShell().setVisible(true);
		setSize();

		setupTransparency();

		// synchronous exec would deactivate the shell right away
		getShell().getDisplay().asyncExec(this::setupInput);
	}

	private void setSize() {
		Point targetSize = target.getSize();
		getShell().setSize(Integer.max(targetSize.x, shellMinSize.x), shellMinSize.y);

		input.setSize(targetSize);

		Control control = tables.getControl();
		int upper = input.getLocation().y + targetSize.y + 1;

		control.setLocation(control.getLocation().x, upper);
		control.setSize(control.getSize().x, shellMinSize.y - upper);
	}

	private void setupTransparency() {
		Region r = new Region();
		r.add(input.getBounds());
		r.add(tables.getControl().getBounds());
		getShell().setRegion(r);
		r.dispose();
	}

	private void setupInput() {
		tables.clearSelection();
		input.setText(target.getText());
		input.setFocus();
		input.setSelection(target.getSelection());
	}

	private void keyReleased(KeyEvent event) {
		switch (event.keyCode) {
			case SWT.ARROW_DOWN:
				tables.move(1);
				break;
			case SWT.ARROW_UP:
				tables.move(-1);
				break;
			case SWT.PAGE_DOWN:
				tables.move(10);
				break;
			case SWT.PAGE_UP:
				tables.move(-10);
				break;
		}
	}

	private void traverse(TraverseEvent event) {
		switch (event.detail) {
			case SWT.TRAVERSE_TAB_NEXT:
			case SWT.TRAVERSE_TAB_PREVIOUS:
			case SWT.TRAVERSE_RETURN:
				accept(event.detail);
				break;
			case SWT.TRAVERSE_ESCAPE:
				focusOut(null);
				break;
		}
	}

	private void accept(Integer traversal) {
		Table table = tables.getSelecedTable();
		if (table != null) {
			tableListener.accept(table);
			focusOut(traversal);
		}
	}


	private void focusOut(Integer traversal) {
		closing = true;
		if (traversal != null) {
			target.traverse(traversal);
		} else {
			target.setFocus();
		}
		getShell().setVisible(false);
		closing = false;
	}

	private class ShellListener extends ShellAdapter {

		@Override
		public void shellClosed(ShellEvent e) {
			/* prevent shell from being disposed by pressing ESCAPE.
			 * Not sure if good idea, seems to work. */
			e.doit = false;
		}

		@Override
		public void shellDeactivated(ShellEvent e) {
			if (!closing) {
				Table selected = tables.getSelecedTable();
				if (selected != null) {
					tableListener.accept(selected);
				}
				getShell().setVisible(false);
			}
			deactivated = formatTime(e);
		}
	}

	private static final int TIMEOUT = 500;
	private long deactivated;

	long getLastDeactivatedTime() {
		return deactivated;
	}

	private long formatTime(TypedEvent event) {
		return event.time & 0xFFFFFFFFL;
	}

	/**
	 * Attach this pop-up to a text input.
	 * 
	 * @param target
	 *            Overlaid text input.
	 * @param tableSupplier
	 *            Supplies table which was previously selected.
	 * @param tableListener
	 *            Notified when table is selected.
	 */
	public void attach(Text target, Supplier<Table> tableSupplier, Consumer<Table> tableListener) {
		Consumer<TypedEvent> opener = e -> {
			if (formatTime(e) - deactivated > TIMEOUT) {
				open(target, tableSupplier.get(), tableListener);
			}
		};
		target.addKeyListener(new KeyEventBlocker(SWT.ARROW_DOWN, SWT.ARROW_UP));
		target.addModifyListener(opener::accept);
		target.addMouseListener(new MouseUpWrapper(opener));
		target.addKeyListener(new KeyReleasedWrapper(SWT.ARROW_DOWN, SWT.NONE, opener));
		target.addKeyListener(new KeyReleasedWrapper(SWT.ARROW_UP, SWT.NONE, opener));
		target.addTraverseListener(e -> {
			switch (e.detail) {
				case SWT.TRAVERSE_RETURN:
					opener.accept(e);
					break;
			}
		});
	}

}

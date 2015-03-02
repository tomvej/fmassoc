package org.tomvej.fmassoc.parts.altsrcdst.popup;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
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
import org.tomvej.fmassoc.core.wrappers.FocusGainedWrapper;
import org.tomvej.fmassoc.core.wrappers.KeyEventBlocker;
import org.tomvej.fmassoc.core.wrappers.KeyReleasedWrapper;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Pop-up window used to select a table. Contains a table list and a text which
 * is used to filter the table list.
 * 
 * @author Tomáš Vejpustek
 */
public class TablePopup {
	private static final int SHELL_STYLE = SWT.MODELESS | SWT.NO_TRIM;

	private final TablePopupTable tables;
	private final Text input;

	private Consumer<Table> tableListener;
	private Text target;

	/**
	 * Specify parent shell.
	 */
	public TablePopup(Shell parent, Point size) {
		Shell popup = new Shell(parent, SHELL_STYLE);
		popup.setLayout(getShellLayout());
		popup.addShellListener(new ShellListener());
		popup.setSize(size);

		input = new Text(popup, SWT.SINGLE | SWT.BORDER);
		input.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		tables = new TablePopupTable(popup, t -> focusOut());

		/* input */
		input.addModifyListener(e -> tables.setFilter(input.getText()));
		input.addKeyListener(new KeyEventBlocker(SWT.ARROW_UP, SWT.ARROW_DOWN));
		input.addTraverseListener(this::traverse);
		input.addKeyListener(new KeyReleasedWrapper(SWT.PAGE_DOWN, SWT.NONE, e -> tables.move(10)));
		input.addKeyListener(new KeyReleasedWrapper(SWT.PAGE_DOWN, SWT.NONE, e -> tables.move(-10)));
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
		this.tables.setTables(tables);
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
		input.setSize(targetSize);

		Control control = tables.getControl();
		int upper = input.getLocation().y + targetSize.y + 1;

		control.setLocation(control.getLocation().x, upper);
		control.setSize(control.getSize().x, getShell().getSize().y - upper);
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
		input.selectAll();
	}

	private void traverse(TraverseEvent event) {
		switch (event.detail) {
			case SWT.TRAVERSE_ARROW_NEXT:
				tables.move(1);
				break;
			case SWT.TRAVERSE_ARROW_PREVIOUS:
				tables.move(-1);
				break;
			case SWT.TRAVERSE_TAB_NEXT:
			case SWT.TRAVERSE_TAB_PREVIOUS:
			case SWT.TRAVERSE_RETURN:
				Table selected = tables.getSelecedTable();
				if (selected == null) {
					event.doit = false;
				} else {
					if (event.detail == SWT.TRAVERSE_RETURN) {
						focusOut();
					} else {
						target.traverse(event.detail, event);
					}
				}
				break;
			case SWT.TRAVERSE_ESCAPE:
				tables.clearSelection();
				focusOut();
				break;
		}
	}

	private void focusOut() {
		target.setFocus();
	}

	private void cleanUp() {
		target = null;
		tableListener = null;
	}

	private class ShellListener extends ShellAdapter {

		@Override
		public void shellClosed(ShellEvent e) {
			/* prevent shell from being closed by pressing ESCAPE.
			 * Not sure if good idea, seems to work. */
			e.doit = false;
		}

		@Override
		public void shellDeactivated(ShellEvent e) {
			deactivated = formatTime(e);
			// for some reason, this event is called twice
			getShell().setVisible(false);
			Table selected = tables.getSelecedTable();
			if (selected != null && tableListener != null) {
				tableListener.accept(selected);
			}
			cleanUp();

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
		target.addFocusListener(new FocusGainedWrapper(e -> {
			if (formatTime(e) - deactivated < TIMEOUT) {
				// if focus is gained too early, relegate it to the parent
				target.getParent().forceFocus();
			} else {
				open(target, tableSupplier.get(), tableListener);
			}
		}));
	}
}

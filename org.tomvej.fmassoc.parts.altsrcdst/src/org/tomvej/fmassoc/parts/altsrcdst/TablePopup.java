package org.tomvej.fmassoc.parts.altsrcdst;

import java.util.Collection;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Region;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.model.db.Table;

public class TablePopup {
	private static final int SHELL_STYLE = SWT.MODELESS | SWT.NO_TRIM;

	private final TablePopupTable tables;
	private final Text input;


	/**
	 * Specify parent shell.
	 */
	public TablePopup(Shell parent) {
		Shell popup = new Shell(parent, SHELL_STYLE);
		popup.setLayout(getShellLayout());

		tables = new TablePopupTable(popup);

		input = new Text(popup, SWT.SINGLE | SWT.BORDER);
		input.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());


		/* input */
		input.addModifyListener(e -> tables.setFilter(input.getText()));
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
		this.tables.setFilter(tables);
	}

	public void open(Text target) {
		getShell().setVisible(true);
		getShell().setLocation(target.getParent().toDisplay(target.getLocation()));

		input.setSize(target.getSize());
		setupTransparency();
	}

	private void setupTransparency() {
		Region r = new Region();
		r.add(input.getBounds());
		r.add(tables.getBounds());
		getShell().setRegion(r);
		r.dispose();
	}
}

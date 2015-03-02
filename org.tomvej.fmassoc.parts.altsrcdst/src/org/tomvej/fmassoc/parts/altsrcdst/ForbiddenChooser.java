package org.tomvej.fmassoc.parts.altsrcdst;

import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.tomvej.fmassoc.model.db.Table;

public class ForbiddenChooser extends Composite {
	private Consumer<Set<Table>> tableListener;

	public ForbiddenChooser(Composite parent) {
		super(parent, SWT.BORDER);
	}

	public void setTables(Collection<Table> tables, Collection<Table> forbidden) {

	}

	public void setTableListener(Consumer<Set<Table>> listener) {
		tableListener = listener;
	}

}

package org.tomvej.fmassoc.parts.altsrcdst;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.tomvej.fmassoc.core.wrappers.FocusGainedWrapper;
import org.tomvej.fmassoc.core.wrappers.FocusLostWrapper;
import org.tomvej.fmassoc.core.wrappers.KeyEventBlocker;
import org.tomvej.fmassoc.core.wrappers.KeyReleasedWrapper;
import org.tomvej.fmassoc.model.db.Table;

/**
 * Component for choosing a table.
 * 
 * @author Tomáš Vejpustek
 */
public class TableChooser extends Composite {
	private final TablePopup popup;
	private final Text input;
	private Table table;

	/**
	 * Specify parent composite and pop-up table chooser.
	 */
	public TableChooser(Composite parent, TablePopup popup) {
		super(parent, SWT.BORDER);
		this.popup = Validate.notNull(popup);

		setLayout(new GridLayout(2, false));

		input = new Text(this, SWT.SINGLE | SWT.BORDER);
		input.setLayoutData(GridDataFactory.fillDefaults().grab(true, false).create());

		input.addFocusListener(new FocusGainedWrapper(e -> startEditing()));
		input.addModifyListener(e -> popup.setFilter(input.getText()));
		input.addTraverseListener(this::traverse);
		input.addKeyListener(new KeyEventBlocker(SWT.ARROW_UP, SWT.ARROW_DOWN));
		input.addKeyListener(new KeyReleasedWrapper(SWT.PAGE_DOWN, SWT.NONE, e -> popup.move(10)));
		input.addKeyListener(new KeyReleasedWrapper(SWT.PAGE_UP, SWT.NONE, e -> popup.move(-10)));
		input.addFocusListener(new FocusLostWrapper(e -> {
			if (!popup.hasFocus()) {
				reject();
			}
		}));

		Button rmBtButton = new Button(this, SWT.PUSH);
		rmBtButton.setText("X");

		setTabList(new Control[] { input });
	}

	private void startEditing() {
		input.selectAll();
		popup.show(input, () -> accept(null, true));
	}

	private void traverse(TraverseEvent event) {
		switch (event.detail) {
			case SWT.TRAVERSE_RETURN:
				accept(event, true);
				break;
			case SWT.TRAVERSE_ESCAPE:
				reject();
				break;
			case SWT.TRAVERSE_ARROW_NEXT:
				popup.move(1);
				break;
			case SWT.TRAVERSE_ARROW_PREVIOUS:
				popup.move(-1);
				break;
			case SWT.TRAVERSE_TAB_NEXT:
			case SWT.TRAVERSE_TAB_PREVIOUS:
				accept(event, false);
				break;
		}
	}

	private void accept(KeyEvent e, boolean forceFocus) {
		Table selected = popup.getSelection();
		if (selected != null) {
			table = selected;
			stopEditing(forceFocus);
			setText();
		} else if (e != null) {
			e.doit = false;
		}
	}

	private void reject() {
		stopEditing(true);
		setText();
	}

	private void stopEditing(boolean forceFocus) {
		if (forceFocus) {
			forceFocus();
		}
		popup.hide();
	}

	private void setText() {
		input.setText(table != null ? table.getName() : "");
	}
}

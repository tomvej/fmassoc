package org.tomvej.fmassoc.parts.status;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ProgressBar;

/**
 * Progress bar which can switch between determinate and indeterminate.
 * 
 * @author Tomáš Vejpustek
 * @see ProgressBar
 */
public class CompoundProgressBar extends Composite {
	private final ProgressBar deterBar;
	private Control indeterBar;
	private final GridData deterData, indeterData;

	/**
	 * Specify parent and style. Use {@link SWT#HORIZONTAL},
	 * {@link SWT#VERTICAL}, {@link SWT#SMOOTH}.
	 * 
	 * @see ProgressBar#ProgressBar(Composite, int)
	 * 
	 */
	public CompoundProgressBar(Composite parent, int style) {
		super(parent, SWT.NONE);

		GridLayout layout = new GridLayout();
		super.setLayout(layout);

		deterBar = new ProgressBar(this, style);
		indeterBar = new ProgressBar(this, style | SWT.INDETERMINATE);

		GridDataFactory data = GridDataFactory.fillDefaults().grab(true, true);
		deterData = data.create();
		deterBar.setLayoutData(deterData);
		indeterData = data.create();
		indeterBar.setLayoutData(indeterData);

		setDeterminate(true);
	}

	@Override
	public void setLayout(Layout layout) {
		// not allowed
	}

	@Override
	public int getStyle() {
		return deterBar.getStyle();
	}

	/**
	 * Set whether this progress bar is determinate or indeterminate.
	 */
	public void setDeterminate(boolean determinate) {
		deterData.exclude = !determinate;
		deterBar.setVisible(determinate);
		indeterData.exclude = determinate;
		indeterBar.setVisible(!determinate);
		layout();
	}

	/**
	 * Check whether this progress bar is determinate or indeterminate.
	 */
	public boolean isDeterminate() {
		return indeterData.exclude;
	}

	/**
	 * Set minimum allow progress value.
	 * 
	 * @see ProgressBar#setMinimum(int)
	 */
	public void setMinimum(int value) {
		deterBar.setMinimum(value);
	}

	/**
	 * Set maximum allowed progress value.
	 * 
	 * @see ProgressBar#setMaximum(int)
	 */
	public void setMaximum(int value) {
		deterBar.setMaximum(value);
	}

	/**
	 * Set current progress value.
	 * 
	 * @see ProgressBar#setSelection(int)
	 */
	public void setSelection(int value) {
		deterBar.setSelection(value);
	}

	/**
	 * Return current progress value.
	 * 
	 * @see ProgressBar#getSelection()
	 */
	public int getSelection() {
		return deterBar.getSelection();
	}

	/**
	 * Return minimum allowed progress value.
	 * 
	 * @see ProgressBar#getMinimum()
	 */
	public int getMinimum() {
		return deterBar.getMinimum();
	}

	/**
	 * Return maximum allowed progress value.
	 * 
	 * @see ProgressBar#getMaximum()
	 */
	public int getMaximum() {
		return deterBar.getMaximum();
	}

	/**
	 * Set minimum, maximum and current progress values.
	 * 
	 * @see ProgressBar#setMinimum(int)
	 * @see ProgressBar#setMaximum(int)
	 * @see ProgressBar#setSelection(int)
	 */
	public void setValues(int minimum, int maximum, int selection) {
		setMinimum(minimum);
		setMaximum(maximum);
		setSelection(selection);
	}

}

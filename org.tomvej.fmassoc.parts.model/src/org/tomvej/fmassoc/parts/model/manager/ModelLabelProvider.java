package org.tomvej.fmassoc.parts.model.manager;

import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.tomvej.fmassoc.parts.model.ModelLoadingException;
import org.tomvej.fmassoc.parts.model.core.ModelEntry;

/**
 * Provides label for model entries in the model manager. Displays model name
 * and error icon for model with inaccessible model loaders.
 * 
 * @author Tomáš Vejpustek
 */
public class ModelLabelProvider extends ColumnLabelProvider {
	private final static int SIZE = 14;
	private final Image error, warn;
	private final Map<ModelEntry, ModelLoadingException> errors;

	/**
	 * Specify model loading exceptions. Initialize icons.
	 */
	public ModelLabelProvider(Map<ModelEntry, ModelLoadingException> errors) {
		error = resize(Display.getCurrent().getSystemImage(SWT.ICON_ERROR), SIZE);
		warn = resize(Display.getCurrent().getSystemImage(SWT.ICON_WARNING), SIZE);
		this.errors = errors;
	}

	private static Image resize(Image source, int size) {
		Image result = new Image(Display.getCurrent(), size, size);
		GC gc = new GC(result);
		gc.setAntialias(SWT.ON);
		gc.setInterpolation(SWT.HIGH);

		gc.drawImage(source, 0, 0, source.getBounds().width, source.getBounds().height, 0, 0, size, size);
		gc.dispose();
		return result;
	}

	@Override
	public void dispose() {
		super.dispose();
		error.dispose();
		warn.dispose();
	}

	private static ModelEntry getElement(Object element) {
		Validate.isInstanceOf(ModelEntry.class, element);
		return (ModelEntry) element;
	}

	@Override
	public String getText(Object element) {
		return getElement(element).getLabel();
	}

	@Override
	public Image getImage(Object element) {
		ModelEntry target = getElement(element);
		if (!target.isValid()) {
			return error;
		} else if (errors.get(target) != null) {
			return warn;
		}
		return null;
	}

	@Override
	public String getToolTipText(Object element) {
		ModelEntry target = getElement(element);
		if (!target.isValid()) {
			return "No associated model loader.";
		} else {
			ModelLoadingException exception = errors.get(target);
			StringBuilder result = new StringBuilder(target.getDescription());
			if (exception != null) {
				result.append("\nUnable to load model: ").append(exception.getLocalizedMessage());
			}
			return result.toString();
		}
	}
}

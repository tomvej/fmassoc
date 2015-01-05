package org.tomvej.fmassoc.parts.paths;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.parts.paths.labelprovider.CustomColumnLabelProvider;

/**
 * Path property label provider which uses {@link CustomColumnLabelProvider} as
 * its basis.
 * 
 * @author Tomáš Vejpustek
 * @param <T>
 *            Path property value type.
 */
public class CustomPathLabelProvider<T> extends ColumnLabelProvider {
	private final CustomColumnLabelProvider<T> provider;
	private final PathProperty<T> property;

	/**
	 * Specify label provider and path property.
	 */
	public CustomPathLabelProvider(CustomColumnLabelProvider<T> provider, PathProperty<T> property) {
		this.provider = provider;
		this.property = property;
	}

	private T getValue(Object element) {
		return property.getValue((Path) element);
	}

	@Override
	public void dispose() {
		super.dispose();
		provider.dispose();
	}

	@Override
	public Color getBackground(Object element) {
		return provider.getBackground(getValue(element));
	}

	@Override
	public Color getForeground(Object element) {
		return provider.getBackground(getValue(element));
	}

	@Override
	public Image getImage(Object element) {
		return provider.getImage(getValue(element));
	}

	@Override
	public String getText(Object element) {
		return provider.getText(getValue(element));
	}

	@Override
	public Color getToolTipBackgroundColor(Object object) {
		return provider.getToolTipBackgroundColor(getValue(object));
	}

	@Override
	public Color getToolTipForegroundColor(Object object) {
		return provider.getToolTipForegroundColor(getValue(object));
	}

	@Override
	public Image getToolTipImage(Object object) {
		return provider.getToolTipImage(getValue(object));
	}

}

package org.tomvej.fmassoc.parts.paths.labelprovider;

import java.util.function.Function;
import java.util.stream.Collectors;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Label provider for path column which constructs text and tool tip text by
 * appending tables and associations in a path.
 * 
 * Table and associations are not separated by spaces, commas, etc. --
 * separators (even simple spaces)
 * must be added (ideally in {@link #getAssociationText(AssociationProperty)}
 * and {@link #getAssociationToolTipText(AssociationProperty)}.
 * 
 * @author Tomáš Vejpustek
 */
public abstract class TextPathLabelProvider extends ColumnLabelProvider {

	private static String getText(Object element, Function<Table, String> tableToString,
			Function<AssociationProperty, String> associationToString) {
		assert element instanceof Path;
		Path target = (Path) element;

		return target.getAssociations().stream()
				.map(a -> associationToString.apply(a) + tableToString.apply(a.getDestination()))
				.collect(Collectors.joining("", tableToString.apply(target.getSource()), ""));
	}

	@Override
	public String getText(Object element) {
		return getText(element, this::getTableText, this::getAssociationText);
	}

	@Override
	public String getToolTipText(Object element) {
		return getText(element, this::getTableToolTipText, this::getAssociationToolTipText);
	}

	/**
	 * Convert table to string for cell text.
	 */
	protected abstract String getTableText(Table target);

	/**
	 * Convert association to string for cell text.
	 */
	protected abstract String getAssociationText(AssociationProperty target);

	/**
	 * Convert table to string for tool tip text.
	 */
	protected abstract String getTableToolTipText(Table target);

	/**
	 * Convert association to string for tool tip text.
	 */
	protected abstract String getAssociationToolTipText(AssociationProperty target);
}

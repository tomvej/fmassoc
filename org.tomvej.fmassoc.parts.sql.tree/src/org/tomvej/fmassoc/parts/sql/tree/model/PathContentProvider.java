package org.tomvej.fmassoc.parts.sql.tree.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Content provider for a path.
 * 
 * @author Tomáš Vejpustek
 */
public class PathContentProvider implements ITreeContentProvider {
	private Map<Table, TableChildren> columns;

	private Stream<Table> getTables(Object input) {
		Validate.isInstanceOf(Path.class, input);
		Path path = (Path) input;
		return Stream.concat(Stream.of(path.getSource()),
				path.getAssociations().stream().map(a -> a.getDestination()));
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement != null) {
			return getTables(inputElement).collect(Collectors.toList()).toArray();
		} else {
			return null;
		}
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof Table) {
			return true;

		} else if (element instanceof TreeNode) {
			return ((TreeNode) element).hasChildren();

		} else if (element instanceof Property) {
			return false;
		}

		throw new IllegalArgumentException("Unknown element type: " + element.getClass());
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Table) {
			return columns.get(parentElement).getChildren();

		} else if (parentElement instanceof TreeNode) {
			return ((TreeNode) parentElement).getChildren();

		} else if (parentElement instanceof Property) {
			return null;
		}

		throw new IllegalArgumentException("Unknown element type: " + parentElement.getClass());
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof TreeNode) {
			return ((TreeNode) element).getParent();

		} else if (element instanceof Property) {
			Property target = (Property) element;
			TableChildren tableChildren = columns.get(target.getParent());
			if (element instanceof AssociationProperty) {
				return tableChildren.getAssociationColumns();
			} else if (VersionProperties.isVersionProperty(target)) {
				return tableChildren.getVersionColumns();
			} else {
				return tableChildren.getPropertyColumns();
			}

		} else if (element instanceof Table) {
			return null;
		}

		throw new IllegalArgumentException("Unknown element type: " + element.getClass());
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			Validate.isInstanceOf(Path.class, newInput);
			Set<AssociationProperty> pathAssociations =
					(((Path) newInput).getAssociations()).stream().collect(Collectors.toSet());
			columns = getTables(newInput).collect(
					Collectors.toMap(Function.identity(), t -> new TableChildren(t, pathAssociations)));
		}
	}

	@Override
	public void dispose() {
		// do nothing
	}

	private <T> Collection<T> getFromTableChildren(Function<TableChildren, T> getter) {
		return columns.values().stream().map(getter).filter(t -> t != null).collect(Collectors.toList());
	}

	/**
	 * Returns all ID_OBJECT proxies.
	 */
	public Collection<ObjectIdColumn> getOidColumns() {
		return getFromTableChildren(TableChildren::getObjectIdColumn);
	}

	/**
	 * Return all proxies for association collections.
	 */
	public Collection<AssociationColumns> getAssociationProxies() {
		return getFromTableChildren(TableChildren::getAssociationColumns);
	}

	/**
	 * Returns all proxies for properties collections.
	 */
	public Collection<PropertyColumns> getPropertyProxies() {
		return getFromTableChildren(TableChildren::getPropertyColumns);
	}

	/**
	 * Returns all proxies for version properties collections.
	 */
	public Collection<VersionColumns> getVersionProxies() {
		return getFromTableChildren(TableChildren::getVersionColumns);
	}
}

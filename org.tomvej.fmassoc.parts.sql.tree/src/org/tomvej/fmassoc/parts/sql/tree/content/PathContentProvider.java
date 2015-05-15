package org.tomvej.fmassoc.parts.sql.tree.content;

import java.util.Arrays;
import java.util.Collections;
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
import org.tomvej.fmassoc.parts.sql.tree.model.AssociationColumns;
import org.tomvej.fmassoc.parts.sql.tree.model.ObjectIdColumn;
import org.tomvej.fmassoc.parts.sql.tree.model.PropertyColumns;
import org.tomvej.fmassoc.parts.sql.tree.model.TableChild;
import org.tomvej.fmassoc.parts.sql.tree.model.VersionColumns;

public class PathContentProvider implements ITreeContentProvider {
	private final Set<String> VERSIONS = Collections.unmodifiableSet(Arrays
			.asList("ID_VERSION", "ID_BRANCH", "ID_PREV_VERSION1", "ID_PREV_VERSION2", "FG_OBJ_DELETED", "ID_USER",
					"TS_USER", "TS_SERVER", "ID_MSG_RCVD", "IND_SYNC", "IND_COMMS_PRIORITY")
			.stream().collect(Collectors.toSet()));
	private Map<Table, TableChildren> columns;

	private Stream<Table> getTables(Object input) {
		Validate.isInstanceOf(Path.class, input);
		Path path = (Path) input;
		return Stream.concat(Stream.of(path.getSource()),
				path.getAssociations().stream().map(a -> a.getDestination()));
	}

	private boolean isVersion(Property property) {
		return VERSIONS.contains(property.getImplName().toUpperCase());
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
		} else if (element instanceof TableChild) {
			return !(element instanceof ObjectIdColumn);
		} else {
			return false;
		}
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Table) {
			return columns.get(parentElement).getChildren();
		} else if (parentElement instanceof AssociationColumns) {
			return ((AssociationColumns) parentElement).getParent().getAssociations().toArray();
		} else if (parentElement instanceof PropertyColumns) {
			return ((PropertyColumns) parentElement).getParent().getProperties().stream().filter(p -> !isVersion(p))
					.collect(Collectors.toList()).toArray();
		} else if (parentElement instanceof VersionColumns) {
			return ((VersionColumns) parentElement).getParent().getProperties().stream().filter(p -> isVersion(p))
					.collect(Collectors.toList()).toArray();
		} else {
			return null;
		}
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof TableChild) {
			return ((TableChild) element).getParent();
		} else if (element instanceof AssociationProperty) {
			return columns.get(((AssociationProperty) element).getParent()).getAssociationColumns();
		} else if (element instanceof Property) {
			TableChildren tableChildren = columns.get(((Property) element).getParent());
			if (isVersion((Property) element)) {
				return tableChildren.getVersionColumns();
			} else {
				return tableChildren.getPropertyColumns();
			}
		} else {
			return null;
		}
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if (newInput != null) {
			columns = getTables(newInput).collect(Collectors.toMap(Function.identity(), t -> new TableChildren(t)));
		}
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// do nothing
	}
}

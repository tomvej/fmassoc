package org.tomvej.fmassoc.model.path;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.model.property.PathPropertyBuilder;

/**
 * Builds path as a stack of associations.
 * 
 * Is not thread safe! All operations are constant-time.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public class PathBuilder extends AbstractPath {
	private final Set<Table> tables = new HashSet<>();
	private final Stack<AssociationProperty> associations = new Stack<>();
	private final Map<PathProperty<?>, PathPropertyBuilder<?>> properties = new HashMap<>();

	/**
	 * Creates an empty path. Note: until it is filled, most methods (
	 * {@link #peek()}, {@link #pop()}, ...) will result in exception.
	 * 
	 * @param properties
	 *            Path properties computed by this path builder.
	 */
	public PathBuilder(Collection<? extends PathProperty<?>> properties) {
		Validate.notNull(properties);
		for (PathProperty<?> property : properties) {
			this.properties.put(property, property.getBuilder());
		}
	}

	/**
	 * Appends association at the end of this path.
	 * 
	 * @return {@code true} when the association was appended, {@code false}
	 *         when its source is not the previous association destination or
	 *         its destination is already along the path (it would be cyclic).
	 */
	public boolean push(AssociationProperty target) {
		Validate.notNull(target);
		if (associations.empty()) {
			tables.add(target.getSource());
		} else {
			// associations must join on tables
			if (!associations.peek().getDestination().equals(target.getSource())) {
				return false;
			}
			// no cycles
			if (tables.contains(target.getDestination())) {
				return false;
			}
		}
		tables.add(target.getDestination());
		associations.push(target);
		for (PathPropertyBuilder<?> builder : properties.values()) {
			builder.push(target);
		}
		return true;
	}

	/**
	 * Returns last association on this path.
	 */
	public AssociationProperty peek() {
		return associations.peek();
	}

	/**
	 * Removes last association from this path.
	 * 
	 * @return Removed association.
	 */
	public AssociationProperty pop() {
		AssociationProperty last = associations.pop();
		tables.remove(last.getDestination());
		for (PathPropertyBuilder<?> builder : properties.values()) {
			builder.pop(last);
		}
		return last;
	}

	/**
	 * Checks whether given table is on this path (even as its source or
	 * destination).
	 */
	public boolean containsTable(Table target) {
		return tables.contains(target);
	}

	@Override
	public Table getSource() {
		return associations.firstElement().getSource();
	}

	@Override
	public Table getDestination() {
		return associations.peek().getDestination();
	}

	@Override
	public List<AssociationProperty> getAssociations() {
		return Collections.unmodifiableList(associations);
	}

	@Override
	public int getLength() {
		return associations.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(PathProperty<T> property) {
		PathPropertyBuilder<T> builder = (PathPropertyBuilder<T>) properties.get(property);
		if (builder == null) {
			return null;
		}
		return builder.getValue();
	}

	/**
	 * Create an immutable copy of current path.
	 */
	public Path createPath() {
		if (getAssociations().isEmpty()) {
			throw new IllegalStateException();
		}
		return new SimplePath(getAssociations(), properties);
	}
}

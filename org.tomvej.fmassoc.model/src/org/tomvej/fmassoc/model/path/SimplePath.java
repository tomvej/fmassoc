package org.tomvej.fmassoc.model.path;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.model.property.PathProperty;
import org.tomvej.fmassoc.model.property.PathPropertyBuilder;

/**
 * Immutable {@link Path} implementation.
 * 
 * @author Tomáš Vejpustek
 *
 */
public class SimplePath extends AbstractPath {
	private final List<AssociationProperty> assoc;
	private final Map<PathProperty<?>, Object> properties = new HashMap<>();

	/**
	 * Specify all associations.
	 * 
	 * @param association
	 *            List of associations comprising this path. This list is copied
	 *            defensively. Note: It is not checked whether the associations
	 *            are connected.
	 */
	public SimplePath(List<AssociationProperty> association) {
		Validate.notEmpty(association);
		assoc = new ArrayList<>(association);
	}

	/**
	 * Specify associations and computed properties. Both are copied
	 * defensively.
	 * 
	 * @param associations
	 *            List of associations comprising this path.
	 * @param properties
	 *            Properties computed for this path.
	 */
	public SimplePath(List<AssociationProperty> associations,
			Map<PathProperty<?>, PathPropertyBuilder<?>> properties) {
		this(associations);
		for (Map.Entry<PathProperty<?>, PathPropertyBuilder<?>> property : properties
				.entrySet()) {
			this.properties.put(property.getKey(), property.getValue()
					.getValue());
		}
	}

	@Override
	public List<AssociationProperty> getAssociations() {
		return Collections.unmodifiableList(assoc);
	}

	@Override
	public Table getDestination() {
		return getAssociations().get(getAssociations().size() - 1)
				.getDestination();
	}

	@Override
	public Table getSource() {
		return getAssociations().get(0).getSource();
	}

	@Override
	public int getLength() {
		return assoc.size();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getProperty(PathProperty<T> property) {
		return (T) properties.get(property);
	}
}
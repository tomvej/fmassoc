package org.tomvej.fmassoc.parts.sql.tree.transform;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;

import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.transform.sql.handles.ColumnHandle;
import org.tomvej.fmassoc.transform.sql.handles.TableHandle;

/**
 * Generates SQL queries wrt selected options only. Used when tree has not yet
 * been built, i.e. directly after a path is selected.
 * 
 * @author Tomáš Vejpustek
 */
public class OptionHandleFactory extends AbstractHandleFactory {
	private static final Collection<Option> TABLE_OPTIONS =
			EnumSet.of(Option.OIDS, Option.ASSOC, Option.PROPERTY);

	/**
	 * Specify selected options.
	 */
	public OptionHandleFactory(Set<Option> options) {
		super(options);
	}

	@Override
	public ColumnHandle getIDPropertyHandle(Table table) {
		return new PropertyHandle(table.getIDImplName(), table) {

			@Override
			public boolean isDisplayed() {
				return isSet(Option.OIDS);
			}
		};
	}

	@Override
	public ColumnHandle getPropertyHandle(AssociationProperty property) {
		return new PropertyHandle(property.getImplName(), property.getParent()) {

			@Override
			public boolean isDisplayed() {
				return isSet(Option.ASSOC);
			}
		};
	}

	@Override
	public ColumnHandle getPropertyHandle(Property property) {
		return new PropertyHandle(property.getImplName(), property.getParent()) {

			@Override
			public boolean isDisplayed() {
				return isSet(Option.PROPERTY);
			}
		};
	}

	@Override
	public TableHandle getTableHandle(Table table) {
		return new TableHandleImpl(table) {

			@Override
			public DisplayState isDisplayed() {
				if (TABLE_OPTIONS.stream().allMatch(o -> isSet(o))) {
					return DisplayState.ALL;
				} else if (TABLE_OPTIONS.stream().anyMatch(o -> isSet(o))) {
					return DisplayState.SELECTED;
				} else {
					return DisplayState.NONE;
				}
			}
		};
	}
}

package org.tomvej.fmassoc.parts.sql.tree.transform;

import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.sql.tree.model.ObjectIdColumn;
import org.tomvej.fmassoc.transform.sql.handles.ColumnHandle;
import org.tomvej.fmassoc.transform.sql.handles.HandleFactory;
import org.tomvej.fmassoc.transform.sql.handles.TableHandle;

/**
 * Generates SQL query wrt elements checked inside a tree viewer.
 * 
 * @author Tomáš Vejpustek
 */
public class TreeHandleFactory implements HandleFactory {
	private CheckboxTreeViewer tree;
	private Set<Option> options;


	/**
	 * Specify tree viewer and selected options.
	 */
	public TreeHandleFactory(CheckboxTreeViewer tree, Set<Option> options) {
		this.tree = Validate.notNull(tree);
		this.options = Validate.notNull(options);
	}

	private boolean isSet(Option option) {
		return options.contains(option);
	}

	@Override
	public TableHandle getTableHandle(Table table) {
		Validate.notNull(table);
		return new TableHandle() {

			@Override
			public String getReference() {
				if (isSet(Option.ABBREV)) {
					return "T" + table.getNumber();
				} else {
					return getDeclaration();
				}
			}

			@Override
			public String getDeclaration() {
				if (isSet(Option.ABBREV)) {
					return table.getImplName() + " AS " + getReference();
				} else {
					return table.getImplName();
				}
			}

			@Override
			public DisplayState isDisplayed() {
				if (!tree.getChecked(table)) {
					return DisplayState.NONE;
				} else if (tree.getGrayed(table)) {
					return DisplayState.SELECTED;
				} else {
					return DisplayState.ALL;
				}
			}
		};
	}

	private abstract class PropertyHandle implements ColumnHandle {
		private final String name;
		private final TableHandle parent;

		public PropertyHandle(String name, Table parent) {
			this.name = name;
			this.parent = getTableHandle(parent);
		}

		@Override
		public String getDeclaration() {
			StringBuilder result = new StringBuilder(parent.getReference()).append(".").append(name);
			if (isSet(Option.PREFIX_COL)) {
				result.append(" AS ").append(getReference());
			}
			return result.toString();
		}

		@Override
		public String getReference() {
			if (isSet(Option.PREFIX_COL)) {
				return parent.getReference() + "_" + name;
			} else {
				return getDeclaration();
			}
		}
	}

	@Override
	public ColumnHandle getPropertyHandle(AssociationProperty property) {
		return getPropertyHandle((Property) property);
	}

	@Override
	public ColumnHandle getPropertyHandle(Property property) {
		return new PropertyHandle(property.getImplName(), property.getParent()) {

			@Override
			public boolean isDisplayed() {
				return tree.getChecked(property);
			}
		};
	}

	@Override
	public ColumnHandle getIDPropertyHandle(Table table) {
		return new PropertyHandle(table.getIDImplName(), table) {

			@Override
			public boolean isDisplayed() {
				return tree.getChecked(ObjectIdColumn.getInstance(table));
			}
		};
	}
}

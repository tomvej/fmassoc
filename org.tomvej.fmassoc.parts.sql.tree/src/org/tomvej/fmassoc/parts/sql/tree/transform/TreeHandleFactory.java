package org.tomvej.fmassoc.parts.sql.tree.transform;

import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.parts.sql.tree.model.ObjectIdColumn;
import org.tomvej.fmassoc.transform.sql.handles.ColumnHandle;
import org.tomvej.fmassoc.transform.sql.handles.TableHandle;

/**
 * Generates SQL query wrt elements checked inside a tree viewer.
 * 
 * @author Tomáš Vejpustek
 */
public class TreeHandleFactory extends AbstractHandleFactory {
	private CheckboxTreeViewer tree;


	/**
	 * Specify tree viewer and selected options.
	 */
	public TreeHandleFactory(CheckboxTreeViewer tree, Set<Option> options) {
		super(options);
		this.tree = Validate.notNull(tree);
	}

	@Override
	public TableHandle getTableHandle(Table table) {
		return new TableHandleImpl(Validate.notNull(table)) {

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

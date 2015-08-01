package org.tomvej.fmassoc.parts.sql.tree.transform;

import java.util.Set;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.transform.sql.handles.ColumnHandle;
import org.tomvej.fmassoc.transform.sql.handles.HandleFactory;
import org.tomvej.fmassoc.transform.sql.handles.TableHandle;

abstract class AbstractHandleFactory implements HandleFactory {
	private final Set<Option> options;

	public AbstractHandleFactory(Set<Option> options) {
		this.options = Validate.notNull(options);
	}

	protected boolean isSet(Option option) {
		return options.contains(option);
	}

	protected abstract class PropertyHandle implements ColumnHandle {
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

	protected abstract class TableHandleImpl implements TableHandle {
		private final Table table;

		public TableHandleImpl(Table table) {
			this.table = table;
		}

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
	}

}

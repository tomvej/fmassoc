package org.tomvej.fmassoc.parts.sql.independent;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.transform.sql.handles.ColumnHandle;
import org.tomvej.fmassoc.transform.sql.handles.HandleFactory;
import org.tomvej.fmassoc.transform.sql.handles.TableHandle;
import org.tomvej.fmassoc.transform.sql.handles.TableHandle.DisplayState;

/**
 * Handle factory for path-independent sql transformer.
 * 
 * @author Tomáš Vejpustek
 */
public class IndependentHandleFactory implements HandleFactory {
	private final Set<String> VERSIONS = Collections.unmodifiableSet(Arrays
			.asList("ID_VERSION", "ID_BRANCH", "ID_PREV_VERSION1", "ID_PREV_VERSION2", "FG_OBJ_DELETED", "ID_USER",
					"TS_USER", "TS_SERVER", "id_msg_rcvd", "ind_sync", "ind_comms_priority")
			.stream().collect(Collectors.toSet()));
	private final Set<Options> options;
	private final Table src, dst;

	/**
	 * Specify selected options and source and destination table.
	 */
	public IndependentHandleFactory(Set<Options> options, Table src, Table dst) {
		this.options = Validate.notNull(options);
		this.src = Validate.notNull(src);
		this.dst = Validate.notNull(dst);
	}

	private boolean isSet(Options option) {
		return options.contains(option);
	}

	/**
	 * Returns whether all columns should be displayed.
	 */
	public boolean displayAllColumns() {
		return displayWholeTables() && !isSet(Options.SRC_DST_ONLY);
	}

	private boolean displayWholeTables() {
		return isSet(Options.PRINT_OIDS) && isSet(Options.PRINT_ASSOC) && isSet(Options.PRINT_VERSIONS)
				&& !isSet(Options.PREFIX_COLUMNS);
	}

	@Override
	public ColumnHandle getIDPropertyHandle(Table table) {
		return new PropertyHandle(table.getIDImplName(), table) {

			@Override
			public boolean isDisplayed() {
				return isSet(Options.PRINT_OIDS);
			}
		};
	}

	@Override
	public ColumnHandle getPropertyHandle(Property property) {
		return new PropertyHandle(property.getImplName(), property.getParent()) {

			@Override
			public boolean isDisplayed() {
				return isSet(Options.PRINT_VERSIONS) || !VERSIONS.contains(property.getImplName());
			}
		};
	}

	@Override
	public ColumnHandle getPropertyHandle(AssociationProperty property) {
		return new PropertyHandle(property.getImplName(), property.getParent()) {

			@Override
			public boolean isDisplayed() {
				return isSet(Options.PRINT_ASSOC);

			}
		};
	}

	private abstract class PropertyHandle implements ColumnHandle {
		private final String name;
		private final Table parent;

		public PropertyHandle(String name, Table parent) {
			this.name = Validate.notBlank(name);
			this.parent = Validate.notNull(parent);
		}

		private boolean appendColumns() {
			return isSet(Options.PREFIX_COLUMNS) && isDisplayed()
					&& !getTableHandle(parent).isDisplayed().equals(DisplayState.NONE);
		}

		@Override
		public String getDeclaration() {
			StringBuilder result = new StringBuilder(getTableHandle(parent).getReference()).append(".").append(name);
			if (appendColumns()) {
				result.append(" AS ").append(getReference());
			}
			return result.toString();
		}

		@Override
		public String getReference() {
			if (appendColumns()) {
				return getTableHandle(parent).getReference() + "_" + name;
			} else {
				return getDeclaration();
			}
		}

	}

	@Override
	public TableHandle getTableHandle(Table table) {
		return new TableHandle() {

			@Override
			public String getReference() {
				if (isSet(Options.ABBREV_TABLES)) {
					return "T" + table.getNumber();
				} else {
					return table.getImplName();
				}
			}

			@Override
			public String getDeclaration() {
				if (isSet(Options.ABBREV_TABLES)) {
					return table.getImplName() + " AS " + getReference();
				} else {
					return table.getImplName();
				}
			}

			@Override
			public DisplayState isDisplayed() {
				if (isSet(Options.SRC_DST_ONLY) && !src.equals(table) && !dst.equals(table)) {
					return DisplayState.NONE;
				}
				if (displayWholeTables()) {
					return DisplayState.ALL;
				}
				return DisplayState.SELECTED;
			}
		};
	}


}

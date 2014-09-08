package org.tomvej.fmassoc.transform.sql.formatters.internal;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Property;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.transform.sql.formatters.JoinFormatter;
import org.tomvej.fmassoc.transform.sql.handles.ColumnHandle;
import org.tomvej.fmassoc.transform.sql.handles.HandleFactory;
import org.tomvej.fmassoc.transform.sql.handles.TableHandle;

/**
 * Auxiliary class for {@link JoinFormatter} which handles formatting of the
 * SELECT expression.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public class SelectExpressionAppender {
	private final StringBuilder builder;
	private final HandleFactory factory;
	private boolean notEmpty = false;

	/**
	 * Specify handle factory and string builder where SELECT expression should
	 * be appended.
	 */
	public SelectExpressionAppender(StringBuilder targetBuilder, HandleFactory handleFactory) {
		builder = Validate.notNull(targetBuilder);
		factory = Validate.notNull(handleFactory);
	}

	/**
	 * Append target table to the SELECT expression.
	 */
	public void append(Table table) {
		TableHandle tableHandle = factory.getTableHandle(table);
		switch (tableHandle.isDisplayed()) {
			case ALL:
				append(new StringBuilder(tableHandle.getReference()).append(".*").toString());
				break;
			case SELECTED:
				appendAllColumns(table);
				break;
			case NONE:
				// do nothing
				break;
			default:
				throw new IllegalArgumentException("Unkown display state.");
		}
	}

	private void appendAllColumns(Table table) {
		appendColumn(factory.getIDPropertyHandle(table));
		for (AssociationProperty assoc : table.getAssociations()) {
			if (!assoc.isReverse()) {
				appendColumn(assoc);
			}
		}
		for (Property prop : table.getProperties()) {
			appendColumn(prop);
		}
	}

	private void appendColumn(Property property) {
		appendColumn(factory.getPropertyHandle(property));
	}

	private void appendColumn(ColumnHandle handle) {
		if (handle.isDisplayed()) {
			append(handle.getDeclaration());
		}
	}

	/**
	 * Check whether any table was actually appended.
	 * 
	 * @return {@code true} when from this instance creation a table has been
	 *         actually appended to the SELECT expression, {@code false}
	 *         otherwise (e.g. due to table display settings).
	 */
	public boolean isEmpty() {
		return !notEmpty;
	}

	private void append(String target) {
		if (!StringUtils.isEmpty(target)) {
			if (notEmpty) {
				builder.append(", ");
			} else {
				notEmpty = true;
			}
			builder.append(target);
		}
	}

}

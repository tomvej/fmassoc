package org.tomvej.fmassoc.transform.sql.formatters;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.transform.sql.formatters.internal.SelectExpressionAppender;
import org.tomvej.fmassoc.transform.sql.handles.HandleFactory;

/**
 * Formats paths as a JOIN query according to a {@link HandleFactory}
 * 
 * @author Tomáš Vejpustek
 * 
 */
public class JoinFormatter {
	private final HandleFactory factory;
	private final boolean allColumns;
	private final boolean leftJoin;

	/**
	 * Specify handle factory.
	 * 
	 * @param displayAllColumns
	 *            when {@code true}, display settings of individual handles are
	 *            suppressed and all columns are displayed ({@code SELECT * FROM}
	 *            )
	 */
	public JoinFormatter(HandleFactory handleFactory, boolean displayAllColumns) {
		this(handleFactory, displayAllColumns, false);
	}

	/**
	 * Specify handle factory.
	 * 
	 * @param displayAllColumns
	 *            when {@code true}, display settings of individual
	 *            handlers are suppressed and all columns are displayed
	 *            (as in {@code SELECT * FROM}).
	 * @param useLeftJoin
	 *            when {@code true} use {@code LEFT JOIN} instead of
	 *            {@code JOIN}
	 */
	public JoinFormatter(HandleFactory handleFactory, boolean displayAllColumns, boolean useLeftJoin) {
		factory = Validate.notNull(handleFactory);
		allColumns = displayAllColumns;
		leftJoin = useLeftJoin;
	}

	/**
	 * Format path with this formatter.
	 */
	public String formatPath(Path target) {
		StringBuilder result = new StringBuilder();
		result.append("SELECT ");
		if (allColumns) {
			result.append("*");
		} else {
			SelectExpressionAppender app = new SelectExpressionAppender(result, factory);
			app.append(target.getSource());
			target.getAssociations().forEach(a -> app.append(a.getDestination()));
			if (app.isEmpty()) {
				result.append("*");
			}
		}

		result.append(" FROM ");
		result.append(factory.getTableHandle(target.getSource()).getDeclaration());
		for (AssociationProperty assoc : target.getAssociations()) {
			if (leftJoin) {
				result.append(" LEFT");
			}
			result.append(" JOIN ");
			result.append(factory.getTableHandle(assoc.getDestination()).getDeclaration());
			result.append(" ON ");
			result.append(factory.getPropertyHandle(assoc).getReference());
			result.append("=");
			result.append(factory.getIDPropertyHandle(assoc.getOther()).getReference());
		}

		return result.toString();
	}

}

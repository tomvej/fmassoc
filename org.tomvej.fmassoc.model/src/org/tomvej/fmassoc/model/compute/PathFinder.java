package org.tomvej.fmassoc.model.compute;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Algorithm for finding path from one table to another.
 * 
 * @author Tomáš Vejpustek
 *
 */
public interface PathFinder {

	/**
	 * Method which finds paths. It is assumed each call of this method runs in
	 * its own thread (most often via a swing worker). Should check
	 * interruptions.
	 * 
	 * @param publisher
	 *            Method used to publish found paths.
	 * @param source
	 *            Source table.
	 * @param destination
	 *            Destination table.
	 * @throws InterruptedException
	 *             When it was interrupted.
	 */
	default void findPaths(Consumer<Path> publisher, Table source, Table destination) throws InterruptedException {
		findPaths(publisher, source, Collections.singletonList(destination), Collections.emptySet());
	}

	/**
	 * <p>
	 * Method which finds paths. It is assumed each call of this method runs in
	 * its own thread (most often via a swing worker). Should check for
	 * interruptions.
	 * </p>
	 * 
	 * <p>
	 * This methods allows to specify tables which have to be lie on the path
	 * and which must not. If a table is in both of those groups, it must lie on
	 * the path.
	 * </p>
	 * 
	 * @param publisher
	 *            Method used to publish found paths.
	 * @param source
	 *            Source table.
	 * @param destinations
	 *            List of table which have to lie on the path in the specified
	 *            order.
	 * @param forbidden
	 *            List of tables which must not lie on the path.
	 * @throws InterruptedException
	 *             When computation was interrupted.
	 */
	void findPaths(Consumer<Path> publisher, Table source, List<Table> destinations, Set<Table> forbidden)
			throws InterruptedException;

	/**
	 * Can be used to validate parameters for finders. Checks parameters are not
	 * null and source and destination table are not equal.
	 */
	static void validateParameters(Consumer<Path> publisher, Table source, Table destination) {
		Validate.notNull(publisher);
		Validate.notNull(source);
		Validate.notNull(destination);
		Validate.isTrue(!source.equals(destination), "Source and destination tables are equal.");
	}

	/**
	 * Can be used to validate parameters for finders.
	 */
	static void validateParameters(Consumer<Path> publisher, Table source, List<Table> destinations, Set<Table> forbidden) {
		Validate.notNull(publisher);
		Validate.notNull(source);
		Validate.notNull(forbidden);
		Set<Table> inter = new HashSet<>(destinations);

		Validate.notEmpty(destinations, "There has to be at least one destination.");
		Validate.isTrue(!forbidden.contains(null), "Forbidden table list contains null element!");
		Validate.isTrue(!inter.contains(null), "Destination table list contains null element(s)!");
		Validate.isTrue(destinations.size() == inter.size(), "Destination table list contains duplicates!");
		Validate.isTrue(!destinations.contains(source), "Destination table list contains source table!");
	}
}

package org.tomvej.fmassoc.model.compute;

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
	 * its own thread
	 * (most often via a swing worker). Should check interruptions.
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
	void findPaths(Consumer<Path> publisher, Table source, Table destination)
			throws InterruptedException;

	/**
	 * Can be used to validate parameters for finders. Checks parameters are not
	 * null and
	 * source and destination table are not equal.
	 */
	static void validateParameters(Consumer<Path> publisher, Table source,
			Table destination) {
		Validate.notNull(publisher);
		Validate.notNull(source);
		Validate.notNull(destination);
		Validate.isTrue(!source.equals(destination),
				"Source and destination tables are equal.");
	}
}

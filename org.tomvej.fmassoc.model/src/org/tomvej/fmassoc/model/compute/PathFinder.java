package org.tomvej.fmassoc.model.compute;

import java.util.function.Consumer;

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
	public void findPaths(Consumer<Path> publisher, Table source,
			Table destination) throws InterruptedException;
}

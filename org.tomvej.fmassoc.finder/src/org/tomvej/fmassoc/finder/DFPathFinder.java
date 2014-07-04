package org.tomvej.fmassoc.finder;

import java.util.Collections;
import java.util.function.Consumer;

import org.tomvej.fmassoc.model.compute.PathFinder;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.model.path.PathBuilder;
import org.tomvej.fmassoc.model.property.PathProperty;

/**
 * Depth-first path finder. Based on depth-first search.
 * 
 * @author Tomáš Vejpustek
 * 
 */
public enum DFPathFinder implements PathFinder {
	/** Singleton instance */
	INSTANCE;
	private static class Computation {
		private final Consumer<Path> publisher;
		private final PathBuilder path = new PathBuilder(
				Collections.<PathProperty<?>> emptyList());
		private final Table source, destination;

		public Computation(Consumer<Path> publisher, Table source,
				Table destination) {
			this.publisher = publisher;
			this.source = source;
			this.destination = destination;
		}

		public void process() throws InterruptedException {
			process(source);
		}

		private void process(Table current) throws InterruptedException {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			if (current.equals(destination)) {
				publisher.accept(path.createPath());
			} else if (!current.isSink()) {
				for (AssociationProperty association : current
						.getAssociations()) {
					if (path.push(association)) {
						process(association.getDestination());
						path.pop();
					}
				}
			}
		}
	}

	@Override
	public void findPaths(Consumer<Path> publisher, Table source,
			Table destination) throws InterruptedException {
		PathFinder.validateParameters(publisher, source, destination);
		new Computation(publisher, source, destination).process();
	}

	/**
	 * Returns an instance of this algorithm.
	 */
	public static PathFinder getInstance() {
		return INSTANCE;
	}
}

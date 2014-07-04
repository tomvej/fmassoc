package org.tomvej.fmassoc.finder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
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
		private final Table source;
		private final List<Table> destinations;
		private final Set<Table> forbidden, inner;
		private PathBuilder path;
		private ListIterator<Table> iter;

		public Computation(Consumer<Path> publisher, Table source,
				List<Table> destinations, Set<Table> forbidden) {
			this.publisher = publisher;
			this.source = source;
			this.destinations = new ArrayList<>(destinations);
			this.forbidden = new HashSet<>(forbidden);
			inner = new HashSet<>(destinations);
		}

		public void process() throws InterruptedException {
			path = new PathBuilder(Collections.<PathProperty<?>> emptyList());
			iter = destinations.listIterator();
			process(source);
		}

		private void process(Table current) throws InterruptedException {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			if (!iter.hasNext()) {
				publisher.accept(path.createPath());
			} else if (!current.isSink()) {
				boolean reached = iter.next().equals(current);
				if (!reached) {
					iter.previous();
					if (forbidden.contains(current) || inner.contains(current)) {
						return;
					}
				}

				for (AssociationProperty association : current
						.getAssociations()) {
					if (path.push(association)) {
						process(association.getDestination());
						path.pop();
						if (reached) {
							iter.previous();
						}
					}
				}
			}
		}
	}

	@Override
	public void findPaths(Consumer<Path> publisher, Table source,
			List<Table> destinations, Set<Table> forbidden)
			throws InterruptedException {
		new Computation(publisher, source, destinations, forbidden).process();
	}

	/**
	 * Returns an instance of this algorithm.
	 */
	public static PathFinder getInstance() {
		return INSTANCE;
	}
}

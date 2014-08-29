package org.tomvej.fmassoc.plugin.prioritydfpathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.tomvej.fmassoc.model.compute.PathFinder;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.model.path.PathBuilder;

/**
 * Extension of simple DF Path finder. Supports two things: Pruning and sort of
 * associations in each {@link Table} (this one is greedy).
 * 
 * @author Tomáš Vejpustek
 * 
 */
public class PriorityDFPathFinder implements PathFinder {
	private final Pruning prune;

	/**
	 * Specify non-{@code null} pruning.
	 */
	public PriorityDFPathFinder(Pruning pruning) {
		prune = Validate.notNull(pruning);
	}


	private class Computation {
		private final Consumer<Path> publisher;
		private final Set<Table> forbid;
		private final PathBuilder path = new PathBuilder(prune.getUsedProperties());

		public Computation(Consumer<Path> publisher, Table source, List<Table> destinations, Set<Table> forbidden)
				throws InterruptedException {
			this.publisher = publisher;
			forbid = Collections.unmodifiableSet(forbidden);

			new SubComputation(Collections.unmodifiableList(destinations)).processNext(source);
		}

		private class SubComputation {
			private final Table dst;
			private final List<Table> destinations;

			public SubComputation(List<Table> destinations) {
				dst = destinations.get(0);
				this.destinations = destinations.subList(1, destinations.size());
			}

			private void process(Table current) throws InterruptedException {
				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
				if (current.equals(dst)) {
					if (destinations.isEmpty()) {
						publisher.accept(path.createPath());
					} else {
						new SubComputation(destinations).processNext(current);
					}
				} else if (!current.isSink() && !forbid.contains(current)) {
					processNext(current);
				}
			}

			public void processNext(Table current) throws InterruptedException {
				for (AssociationProperty association : current.getAssociations()) {
					if (path.push(association)) {
						if (prune.prune(path)) {
							process(association.getDestination());
						}
						path.pop();
					}
				}
			}

		}
	}

	@Override
	public void findPaths(Consumer<Path> publisher, Table source, List<Table> destinations, Set<Table> forbidden)
			throws InterruptedException {
		PathFinder.validateParameters(publisher, source, destinations, forbidden);
		Set<Table> forbid = new HashSet<Table>(forbidden);
		forbid.addAll(destinations);
		new Computation(publisher, source, new ArrayList<>(destinations), forbid);
	}
}

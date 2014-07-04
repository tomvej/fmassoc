package org.tomvej.fmassoc.finder.priority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
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
		private final Consumer<Path> publish;
		private final Table src;
		private final List<Table> dst;
		private final Set<Table> forbid, inner;
		private PathBuilder path;
		private ListIterator<Table> iter;

		public Computation(Consumer<Path> publisher, Table source,
				List<Table> destinations, Set<Table> forbidden) {
			publish = publisher;
			src = source;
			dst = new ArrayList<>(destinations);
			inner = new HashSet<>(destinations);
			forbid = new HashSet<>(forbidden);
		}

		public void process() throws InterruptedException {
			path = new PathBuilder(prune.getUsedProperties());
			iter = dst.listIterator();
			process(src);
		}

		private void process(Table current) throws InterruptedException {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			if (!iter.hasNext()) {
				publish.accept(path.createPath());
			} else if (!current.isSink()) {
				boolean reached = iter.next().equals(current);
				if (!reached) {
					iter.previous();
					if (forbid.contains(current) || inner.contains(current)) {
						return;
					}
				}

				for (AssociationProperty association : current
						.getAssociations()) {
					if (path.push(association)) {
						if (!prune.prune(path)) {
							process(association.getDestination());
						}
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
		PathFinder.validateParameters(publisher, source, destinations,
				forbidden);
		new Computation(publisher, source, destinations, forbidden).process();
	}

}

package org.tomvej.fmassoc.finder.priority;

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
		private final Table src, dst;
		private final PathBuilder path = new PathBuilder(
				prune.getUsedProperties());

		public Computation(Consumer<Path> publisher, Table source,
				Table destination) {
			publish = publisher;
			src = source;
			dst = destination;
		}

		public void process() throws InterruptedException {
			process(src);
		}

		private void process(Table current) throws InterruptedException {
			if (Thread.interrupted()) {
				throw new InterruptedException();
			}
			if (current.equals(dst)) {
				publish.accept(path.createPath());
			} else if (!current.isSink()) {
				for (AssociationProperty association : current
						.getAssociations()) {
					if (path.push(association)) {
						if (!prune.prune(path)) {
							process(association.getDestination());
						}
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

}

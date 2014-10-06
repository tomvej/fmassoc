package org.tomvej.fmassoc.plugin.prioritydfpathfinder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.tomvej.fmassoc.core.search.PathFinder;
import org.tomvej.fmassoc.model.db.AssociationProperty;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.model.path.Path;
import org.tomvej.fmassoc.model.path.PathBuilder;

/**
 * Extension of simple DF Path finder. Supports pruning.
 * 
 * @author Tomáš Vejpustek
 * 
 */
class PriorityDFPathFinder implements PathFinder {
	private final Pruning prune;
	private final Table source;
	private final Set<Table> forbid;
	private final Set<Table> inner;
	private final List<Table> destinations;

	/**
	 * Specify non-{@code null} pruning.
	 */
	public PriorityDFPathFinder(Pruning pruning, Table source, List<Table> destinations, Set<Table> forbidden) {
		this.prune = pruning;
		this.source = source;
		this.destinations = destinations;
		this.forbid = forbidden;
		this.inner = Collections.unmodifiableSet(new HashSet<>(destinations));
	}

	private class Computation {
		private final Consumer<Path> publisher;
		private final IProgressMonitor monitor;
		private final PathBuilder path = new PathBuilder(prune.getUsedProperties());

		public Computation(Consumer<Path> publisher, IProgressMonitor monitor) {
			this.publisher = Validate.notNull(publisher);
			this.monitor = Validate.notNull(monitor);

			new SubComputation(destinations).processNext(source);
		}

		private class SubComputation {
			private final Table dst;
			private final List<Table> destinations;

			public SubComputation(List<Table> destinations) {
				dst = destinations.get(0);
				this.destinations = destinations.subList(1, destinations.size());
			}

			private void process(Table current) {
				if (monitor.isCanceled()) {
					throw new OperationCanceledException();
				}
				if (current.equals(dst)) {
					if (destinations.isEmpty()) {
						publisher.accept(path.createPath());
					} else {
						new SubComputation(destinations).processNext(current);
					}
				} else if (!forbid.contains(current) && !inner.contains(current)) {
					processNext(current);
				}
			}

			public void processNext(Table current) {
				for (AssociationProperty association : current.getAssociations()) {
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
	public IStatus run(Consumer<Path> publisher, IProgressMonitor monitor) {
		new Computation(publisher, monitor);
		return Status.OK_STATUS;
	}

}

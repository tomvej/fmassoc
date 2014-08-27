package org.tomvej.fmassoc.plugin.dfpathfinder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.function.Consumer;

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
 * Simple depth-first path finder. Includes multiple destinations and forbidden
 * tables.
 * 
 * @author Tomáš Vejpustek
 */
public class DFPathFinder implements PathFinder {
	private final Table source;
	private final List<Table> destinations;
	private final Set<Table> forbidden, inner;

	/**
	 * Specify search parameters.
	 */
	public DFPathFinder(Table source, List<Table> destinations, Set<Table> forbidden) {
		this.source = source;
		this.destinations = new ArrayList<>(destinations);
		this.forbidden = new HashSet<>(forbidden);
		inner = new HashSet<>(destinations);
	}

	private class Computation {
		private final IProgressMonitor monitor;
		private final PathBuilder path;
		private final ListIterator<Table> iter;
		private final Consumer<Path> publisher;

		public Computation(IProgressMonitor monitor, Consumer<Path> publisher) {
			this.monitor = monitor;
			this.publisher = publisher;
			path = new PathBuilder(Collections.emptyList());
			iter = destinations.listIterator();
		}


		public void process(Table current) {
			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}

			if (!iter.hasNext()) {
				publisher.accept(path.createPath());
			} else {
				boolean reached = iter.next().equals(current);
				if (!reached) {
					iter.previous();
					if (forbidden.contains(current) || inner.contains(current)) {
						return;
					}
				}

				for (AssociationProperty association : current.getAssociations()) {
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
	public IStatus run(Consumer<Path> publisher, IProgressMonitor monitor) {
		new Computation(monitor, publisher).process(source);

		return Status.OK_STATUS;
	};
}

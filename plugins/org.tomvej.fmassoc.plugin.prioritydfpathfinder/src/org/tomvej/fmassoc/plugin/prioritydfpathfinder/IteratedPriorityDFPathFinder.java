package org.tomvej.fmassoc.plugin.prioritydfpathfinder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.apache.commons.lang3.Validate;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.tomvej.fmassoc.core.search.PathFinder;
import org.tomvej.fmassoc.model.db.Table;
import org.tomvej.fmassoc.model.path.Path;

/**
 * Path finder which iterates pruning. Extends functionality of
 * {@link PriorityDFPathFinder}.
 * 
 * Takes sequence of pruning ({@code Iterable<Pruning>}). For each pruning in
 * sequence finds all paths not pruned by it. Each path is published only once.
 * 
 * It is advised for pruning sequence to be ordered by decreasing strictness.
 * 
 * @author Tomáš Vejpustek
 * @see PriorityDFPathFinder
 */
class IteratedPriorityDFPathFinder implements PathFinder {
	private static class SetConsumer implements Consumer<Path> {
		private final Set<Path> published = new HashSet<>();
		private final Consumer<Path> publisher;

		public SetConsumer(Consumer<Path> publisher) {
			Validate.notNull(publisher);

			this.publisher = publisher;
		}

		@Override
		public void accept(Path target) {
			if (published.add(target)) {
				publisher.accept(target);
			}
		}
	}

	private final Iterable<Pruning> prune;
	private final Table source;
	private final List<Table> destinations;
	private final Set<Table> forbidden;

	/**
	 * Specify non-{@code null} pruning sequence.
	 */
	public IteratedPriorityDFPathFinder(Iterable<Pruning> pruning, Table source, List<Table> destinations,
			Set<Table> forbidden) {
		prune = Validate.noNullElements(pruning);
		this.source = source;
		this.destinations = destinations;
		this.forbidden = forbidden;
	}


	@Override
	public IStatus run(Consumer<Path> publisher, IProgressMonitor monitor) {
		Consumer<Path> setPublisher = new SetConsumer(publisher);
		for (Pruning pruning : prune) {
			new PriorityDFPathFinder(pruning, source, destinations, forbidden).run(setPublisher, monitor);
		}
		return Status.OK_STATUS;
	}

}

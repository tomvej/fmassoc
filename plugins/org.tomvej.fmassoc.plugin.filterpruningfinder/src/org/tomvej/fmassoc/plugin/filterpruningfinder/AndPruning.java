package org.tomvej.fmassoc.plugin.filterpruningfinder;

import java.util.Collection;
import java.util.HashSet;

import org.tomvej.fmassoc.model.path.PathInfo;
import org.tomvej.fmassoc.plugin.prioritydfpathfinder.AbstractPruning;
import org.tomvej.fmassoc.plugin.prioritydfpathfinder.Pruning;

public class AndPruning extends AbstractPruning {
	private final Pruning p1, p2;

	private static <T> Collection<T> union(Collection<T> t1, Collection<T> t2) {
		Collection<T> result = new HashSet<>(t1);
		result.addAll(t2);
		return result;
	}

	public AndPruning(Pruning p1, Pruning p2) {
		super(union(p1.getUsedProperties(), p2.getUsedProperties()));
		this.p1 = p1;
		this.p2 = p2;
	}

	@Override
	public boolean prune(PathInfo target) {
		return p1.prune(target) || p2.prune(target);
	}
}

package org.tomvej.fmassoc.plugin.simplepruningfinder;

import java.util.Collections;
import java.util.List;

import org.tomvej.fmassoc.plugin.prioritydfpathfinder.IteratedPriorityDFFinderProvider;
import org.tomvej.fmassoc.plugin.prioritydfpathfinder.Pruning;

/**
 * Path finder provider. Specifies pruning iteration.
 * 
 * @author Tomáš Vejpustek
 */
public class SimplePathFinderProvider extends IteratedPriorityDFFinderProvider {

	/**
	 * Specify settings.
	 */
	public SimplePathFinderProvider(Settings settings) {
		super(generatePruning(settings));
	}

	private static List<Pruning> generatePruning(Settings settings) {
		// TODO
		return Collections.emptyList();
	}

}
